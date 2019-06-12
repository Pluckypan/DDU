package engineer.echo.whisper.p2p

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.*
import android.os.Looper
import engineer.echo.easylib.formatLog
import engineer.echo.whisper.WhisperConnectionInfo
import engineer.echo.whisper.WhisperDevice
import engineer.echo.whisper.WhisperGroup

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

        fun WifiP2pInfo.toWhisperConnectionInfo(): WhisperConnectionInfo {
            return WhisperConnectionInfo(
                groupFormed,
                isGroupOwner,
                groupOwnerAddress
            )
        }

        fun WifiP2pGroup.toWhisperGroup(): WhisperGroup {
            return WhisperGroup(networkName,
                passphrase,
                isGroupOwner,
                owner.toWhisperDevice(),
                clientList.map {
                    it.toWhisperDevice()
                })
        }

        fun print(format: String, vararg args: Any) {
            format.formatLog("Whisper", *args)
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
        print("onStateChanged %s", enable)
        listener?.onDeviceAvailable(enable)
    }

    override fun onConnectionChanged(connected: Boolean) {
        print("onConnectionChanged %s", connected)
        listener?.onDeviceConnectionChanged(connected)
    }

    override fun onThisDeviceChanged(device: WifiP2pDevice) {
        print("onDeviceInfoChanged %s", device.deviceAddress)
        listener?.onDeviceInfoChanged(device.toWhisperDevice())
    }

    override fun onDeviceListChanged(peers: WifiP2pDeviceList) {
        print("onDeviceListChanged %s", peers.deviceList.size)
        listener?.onDeviceListChanged(peers.toWhisperDeviceList())
    }

    override fun onConnectionInfoChanged(info: WifiP2pInfo) {
        print(
            "onConnectionInfoChanged %s %s %s",
            info.groupOwnerAddress.hostAddress,
            info.isGroupOwner,
            info.groupFormed
        )
        listener?.onDeviceConnectionInfoChanged(info.toWhisperConnectionInfo())
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

    fun discover(action: ((result: Boolean) -> Unit)? = null) {
        channel?.let {
            manager?.discoverPeers(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    action?.invoke(true)
                    print("discover %s", "init onSuccess")
                }

                override fun onFailure(reason: Int) {
                    action?.invoke(false)
                    print("discover %s %d", "init onFailure", reason)
                }
            })
        }
    }

    fun cancelDiscover(action: ((result: Boolean) -> Unit)? = null) {
        channel?.let {
            manager?.stopPeerDiscovery(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    action?.invoke(true)
                    print("cancel discover %s", "init onSuccess")
                }

                override fun onFailure(reason: Int) {
                    action?.invoke(false)
                    print("cancel discover %s %d", "init onFailure", reason)
                }
            })
        }
    }

    fun connect(address: String, action: ((result: Boolean) -> Unit)? = null) {
        channel?.let {
            manager?.connect(it, WifiP2pConfig().apply {
                deviceAddress = address
            }, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    action?.invoke(true)
                    print("connect %s %s", "init onSuccess", address)
                }

                override fun onFailure(reason: Int) {
                    action?.invoke(false)
                    print("connect %s %s", "init onSuccess", address)
                }
            })
        }
    }

    fun cancelConnect(action: ((result: Boolean) -> Unit)? = null) {
        channel?.let {
            manager?.cancelConnect(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    action?.invoke(true)
                    print("cancelConnect %s", "onSuccess")
                }

                override fun onFailure(reason: Int) {
                    action?.invoke(false)
                    print("cancelConnect %s %d", "onFailure", reason)
                }
            })
        }
    }

    fun disConnect(action: ((result: Boolean) -> Unit)? = null) {
        channel?.let {
            manager?.removeGroup(it, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    action?.invoke(true)
                    print("disConnect %s", "onSuccess")
                }

                override fun onFailure(reason: Int) {
                    action?.invoke(false)
                    print("disConnect %s %d", "onFailure", reason)
                }
            })
        }
    }

    fun requestGroupInfo(action: ((group: WhisperGroup) -> Unit)?) {
        channel?.let { c ->
            manager?.requestGroupInfo(c) { grp ->
                action?.invoke(grp.toWhisperGroup().also {
                    print("requestGroupInfo %s", it.toString())
                })
            }
        }
    }

    fun setListener(listener: WifiTransferListener? = null) {
        this.listener = listener
    }
}