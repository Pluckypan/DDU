package engineer.echo.whisper.p2p

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.*
import android.os.Looper
import android.util.Log
import engineer.echo.whisper.WhisperDevice

class WifiTransfer(val app: Application) : WifiReceiverListener {

    companion object {

        fun WifiP2pDevice.toWhisperDevice(): WhisperDevice {
            return WhisperDevice(
                deviceName,
                deviceAddress,
                primaryDeviceType,
                status,
                isGroupOwner
            )
        }

        fun WifiP2pDeviceList.toWhisperDeviceList(): List<WhisperDevice> {
            return this.deviceList.map {
                WhisperDevice(
                    it.deviceName,
                    it.deviceAddress,
                    it.primaryDeviceType,
                    it.status,
                    it.isGroupOwner
                )
            }
        }

        fun print(format: String, vararg args: Any) {
            val msg = format.format(*args)
            Log.d("Whisper", msg)
        }
    }

    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        app.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    private var listener: WifiTransferListener? = null

    private val intentFilter = IntentFilter().apply {
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
        print("onStateChanged %s", enable, "XXX")
    }

    override fun onConnectionChanged(connected: Boolean) {
        print("onConnectionChanged %s", connected)
    }

    override fun onThisDeviceChanged(device: WifiP2pDevice) {
        print("onThisDeviceChanged %s", device.deviceAddress)
        listener?.onThisDeviceChanged(device.toWhisperDevice())
    }

    override fun onDeviceListChanged(peers: WifiP2pDeviceList) {
        print("onDeviceListChanged %s", peers.deviceList.size)
        listener?.onDeviceListChanged(peers.toWhisperDeviceList())
    }

    override fun onConnectionInfoChanged(info: WifiP2pInfo) {
        print("onConnectionInfoChanged %s", info.groupOwnerAddress.address)
    }

    fun register() {
        receiver?.let {
            app.registerReceiver(it, intentFilter)
        }
    }

    fun unregister() {
        receiver?.let {
            try {
                app.unregisterReceiver(it)
            } catch (e: Exception) {

            }
        }
    }

    fun discover() {
        channel?.let {
            manager?.discoverPeers(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    print("discover %s", "init onSuccess")
                }

                override fun onFailure(reason: Int) {
                    print("discover %s %d", "init onFailure", reason)
                }
            })
        }
    }

    fun cancelDiscover() {
        channel?.let {
            manager?.stopPeerDiscovery(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    print("cancel discover %s", "init onSuccess")
                }

                override fun onFailure(reason: Int) {
                    print("cancel discover %s %d", "init onFailure", reason)
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

    fun setListener(listener: WifiTransferListener? = null) {
        this.listener = listener
    }
}