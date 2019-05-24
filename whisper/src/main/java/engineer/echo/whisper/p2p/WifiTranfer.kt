package engineer.echo.whisper.p2p

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.*
import android.os.Looper

class WifiTranfer(val app: Application) : WifiReceiverListener {

    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        app.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null

    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    init {
        channel = manager?.initialize(app, Looper.getMainLooper(), null)
        if (channel != null && manager != null) {
            receiver = WifiReceiver(manager!!, channel!!, this)
        }
    }

    override fun onStateChanged(enable: Boolean) {

    }

    override fun onConnectionChanged(connected: Boolean) {

    }

    override fun onThisDeviceChanged(device: WifiP2pDevice) {

    }

    override fun onDeviceListChanged(peers: WifiP2pDeviceList) {

    }

    override fun onConnectionInfoChanged(info: WifiP2pInfo) {

    }

    fun register() {
        receiver?.let {
            app.registerReceiver(it, intentFilter)
        }
    }

    fun unregister() {
        receiver?.let {
            app.unregisterReceiver(it)
        }
    }

    fun discover() {
        channel?.let {
            manager?.discoverPeers(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {

                }

                override fun onFailure(reason: Int) {

                }
            })
        }
    }

    fun connect(config: WifiP2pConfig) {
        channel?.let {
            manager?.connect(it, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {

                }

                override fun onFailure(reason: Int) {

                }
            })
        }
    }
}