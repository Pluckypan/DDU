package engineer.echo.whisper.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Parcelable

class WifiReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val listener: WifiReceiverListener
) : BroadcastReceiver(), WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // 可以用于判断 WIFI P2P 功能是否可用
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    listener.onStateChanged(true)
                } else {
                    listener.onStateChanged(false)
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // 请求可用列表
                manager.requestPeers(channel, this)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                (intent.getParcelableExtra<Parcelable>(WifiP2pManager.EXTRA_NETWORK_INFO) as NetworkInfo).also {
                    if (it.isConnected) {
                        listener.onConnectionChanged(true)
                        // 连接成功之后 获取 Group Owner 信息(ip地址)
                        manager.requestConnectionInfo(channel, this)
                    } else {
                        listener.onConnectionChanged(false)
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                (intent.getParcelableExtra<Parcelable>(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice).also {
                    listener.onThisDeviceChanged(it)
                }
            }
        }
    }

    /**
     * [WifiP2pManager.requestPeers] 回调
     * 用于获取设备列表
     */
    override fun onPeersAvailable(peers: WifiP2pDeviceList) {
        listener.onDeviceListChanged(peers)
    }

    /**
     * [WifiP2pManager.requestConnectionInfo] 回调
     * 用于获取连接信息
     */
    override fun onConnectionInfoAvailable(info: WifiP2pInfo) {
        listener.onConnectionInfoChanged(info)
    }
}