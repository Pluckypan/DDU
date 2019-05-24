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
import engineer.echo.study.R
import engineer.echo.study.databinding.WifiP2pBinding

@Configuration(theme = R.style.Theme_AppCompat_Light)
class WifiP2pFragment : MasterFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(WifiP2pFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: WifiP2pBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_p2p, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            rcvPeersIpc.layoutManager = LinearLayoutManager(context)
            rcvPeersIpc.adapter = WifiListAdapter()
        }
    }
}