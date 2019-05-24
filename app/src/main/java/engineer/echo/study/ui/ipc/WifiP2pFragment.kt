package engineer.echo.study.ui.ipc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.App
import engineer.echo.study.R
import engineer.echo.study.databinding.WifiP2pBinding
import engineer.echo.whisper.WhisperDevice
import engineer.echo.whisper.p2p.WifiTranfer
import engineer.echo.whisper.p2p.WifiTransferListener

@Configuration(theme = R.style.Theme_AppCompat_Light)
class WifiP2pFragment : MasterFragment(), WifiTransferListener {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(WifiP2pFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: WifiP2pBinding

    private val mWifiTransfer = WifiTranfer(App.getApp()).apply {
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
                mWifiTransfer.discover()
            }
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
        mBinding.rcvPeersIpc.adapter.let {
            if (it is WifiListAdapter) {
                it.setData(deviceList)
            }
        }
    }

    override fun onThisDeviceChanged(device: WhisperDevice) {
        mBinding.currentDevice = "${device.name} & ${device.address}"
    }
}