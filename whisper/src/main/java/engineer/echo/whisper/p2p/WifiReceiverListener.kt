package engineer.echo.whisper.p2p

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo

interface WifiReceiverListener {
    fun onStateChanged(enable: Boolean)
    fun onConnectionChanged(connected: Boolean)
    fun onThisDeviceChanged(device: WifiP2pDevice)
    fun onDeviceListChanged(peers: WifiP2pDeviceList)
    fun onConnectionInfoChanged(info: WifiP2pInfo)
}