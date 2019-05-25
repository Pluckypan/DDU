package engineer.echo.study.ui.ipc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.App
import engineer.echo.study.R
import engineer.echo.study.databinding.WifiP2pBinding
import engineer.echo.whisper.WhisperDevice
import engineer.echo.whisper.p2p.WifiTransfer
import engineer.echo.whisper.p2p.WifiTransferListener

@Configuration(theme = R.style.Theme_AppCompat_Light)
class WifiP2pFragment : MasterFragment(), WifiTransferListener {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(WifiP2pFragment::class.java).also {
                fragment.startFragment(it)
            }
        }

        @JvmStatic
        @BindingAdapter("searchState")
        fun bindSearchText(textView: TextView, searchState: Int) {
            when (searchState) {
                0 -> R.string.label_search_ipc
                1 -> R.string.label_stop_ipc
                else -> R.string.label_search_ipc
            }.also {
                textView.setText(it)
            }
        }
    }

    private lateinit var mBinding: WifiP2pBinding

    private val mWifiTransfer = WifiTransfer(App.getApp()).apply {
        setListener(this@WifiP2pFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_p2p, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            rcvPeersIpc.layoutManager = LinearLayoutManager(context)
            rcvPeersIpc.adapter = WifiListAdapter()
            tvSearchP2p.setOnClickListener {
                if (searchState == 1) {
                    searchState = 0
                    mWifiTransfer.cancelDiscover()
                } else {
                    searchState = 1
                    mWifiTransfer.discover()
                }
            }
            searchState = 0
        }
    }

    override fun onPause() {
        super.onPause()
        mWifiTransfer.unregister()
    }

    override fun onResume() {
        super.onResume()
        mWifiTransfer.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWifiTransfer.setListener(null)
    }

    override fun onDeviceListChanged(deviceList: List<WhisperDevice>) {
        mBinding.searchState = 0
        mBinding.rcvPeersIpc.adapter.let {
            if (it is WifiListAdapter) {
                it.setData(deviceList)
            }
        }
    }

    override fun onThisDeviceChanged(device: WhisperDevice) {
        mBinding.currentDevice = "${device.name}\n${device.address}"
    }
}