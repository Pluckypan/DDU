package engineer.echo.study.ui.ipc

import android.Manifest
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
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.cmpts.bottomIn
import engineer.echo.study.cmpts.bottomOut
import engineer.echo.study.cmpts.getColor
import engineer.echo.study.cmpts.toPx
import engineer.echo.study.databinding.WifiP2pBinding
import engineer.echo.study.ui.ProtoBufFragment.Companion.toUser
import engineer.echo.whisper.WhisperConnectionInfo
import engineer.echo.whisper.WhisperDevice
import engineer.echo.whisper.p2p.WifiClientTask
import engineer.echo.whisper.p2p.WifiServerTask
import engineer.echo.whisper.p2p.WifiTransfer
import engineer.echo.whisper.p2p.WifiTransferListener
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayInputStream

@Configuration(theme = R.style.Theme_AppCompat_Light)
class WifiP2pFragment : MasterFragment(), WifiTransferListener, WifiListAdapter.WifiListAdapterListener {

    companion object {

        private const val REQ_WIFI = 10010

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

        @JvmStatic
        @BindingAdapter("selectDevice", "connection")
        fun bindSelectDevice(
            textView: TextView,
            device: WhisperDevice? = null,
            connection: WhisperConnectionInfo? = null
        ) {
            val sb = StringBuilder()
            device?.apply {
                sb.appendln(toString())
            }
            connection?.apply {
                sb.appendln(toString())
                if (groupOwner && groupFormed) {
                    sb.appendln("as server")
                } else if (groupFormed) {
                    sb.appendln("as client")
                }
            }
            textView.text = sb.toString()
        }

        @JvmStatic
        @BindingAdapter("connected")
        fun bindConnection(view: View, connected: Boolean? = null) {
            if (connected == true) {
                view.isSelected = true
                R.string.label_disconnect_ipc
            } else {
                view.isSelected = false
                R.string.label_connect_ipc
            }.also {
                if (view is TextView) {
                    view.setText(it)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("connection")
        fun bindClientServer(view: View, connection: WhisperConnectionInfo? = null) {
            connection?.let {
                if (it.groupOwner && it.groupFormed) {
                    view.visibility = View.INVISIBLE
                } else {
                    view.visibility = View.VISIBLE
                }
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
            rcvPeersIpc.adapter = WifiListAdapter().also {
                it.setListener(this@WifiP2pFragment)
            }
            tvSearchP2p.setOnClickListener {
                requirePermission {
                    if (searchState == 1) {
                        searchState = 0
                        mWifiTransfer.cancelDiscover()
                    } else {
                        searchState = 1
                        mWifiTransfer.discover()
                    }
                }
            }
            searchState = 0

            tvConnectP2p.setOnClickListener {
                mBinding.selectDevice?.let {
                    requirePermission {
                        if (connected == true) {
                            mWifiTransfer.disConnect()
                        } else {
                            mWifiTransfer.connect(it.address)
                        }
                    }
                }
            }
            layoutDetailIpc.setOnClickListener {
                it.bottomOut()
            }

            tvSelfDeviceIpc.setOnClickListener {
                if (selectDevice != null) {
                    layoutDetailIpc.bottomIn()
                } else {
                    if (connected == true) {
                        mWifiTransfer.requestGroupInfo {
                            selectDevice = it.owner
                            layoutDetailIpc.bottomIn()
                        }
                    }
                }
            }
            syntaxTransferApp.code.apply {
                layoutParams.height = 60f.toPx()
                textSize = 14f
            }
            transferInfo = "idle"

            tvSendP2p.setOnClickListener {
                connection?.let { info ->
                    val stream = C.newUser().toByteArray()
                    WifiClientTask(info.groupAddress.hostAddress, onBegin = {
                        transferInfo = "sending..."
                    }, onProgress = {
                        transferInfo = "sending...(%$it)"
                    }, onResult = {
                        transferInfo = "sending ${if (it) "success" else "failed"}"
                    }).execute(ByteArrayInputStream(stream))
                }
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
        mBinding.searchState = 0
        mBinding.rcvPeersIpc.adapter.let {
            if (it is WifiListAdapter) {
                it.setData(deviceList)
            }
        }
    }

    override fun onDeviceInfoChanged(device: WhisperDevice) {
        mBinding.currentDevice = "${device.name}\n${device.address}\n${device.statusDesc()}"
    }

    override fun onDeviceAvailable(enable: Boolean) {
        mBinding.tvSelfDeviceIpc.apply {
            if (enable) {
                setTextColor(getColor(R.color.color333333))
            } else {
                setTextColor(getColor(R.color.colorCC0033))
            }
        }
    }

    override fun onDeviceConnectionChanged(connected: Boolean) {
        mBinding.connected = connected
    }

    override fun onDeviceConnectionInfoChanged(info: WhisperConnectionInfo) {
        mBinding.connection = info
        if (info.groupOwner && info.groupFormed) {
            WifiServerTask(onResult = {
                val user = it?.toUser()
                mBinding.transferInfo = "received ${user?.name} ${user?.extra}"
            }, onBegin = {
                mBinding.transferInfo = "receiving..."
            }).execute()
        }
    }

    override fun onItemClick(device: WhisperDevice) {
        mBinding.selectDevice = device
        mBinding.layoutDetailIpc.bottomIn()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQ_WIFI)
    private fun requirePermission(action: (() -> Unit)? = null) {
        if (EasyPermissions.hasPermissions(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            action?.invoke()
        } else {
            EasyPermissions.requestPermissions(this, "WiFi P2P", REQ_WIFI, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}