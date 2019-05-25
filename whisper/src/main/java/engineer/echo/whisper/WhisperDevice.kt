package engineer.echo.whisper

import android.net.wifi.p2p.WifiP2pDevice

class WhisperDevice(
    val name: String,
    val address: String,
    val type: String,
    val status: Int,
    val groupOwner: Boolean
) {

    fun statusDesc(): String {
        return when (status) {
            WifiP2pDevice.AVAILABLE -> "Available"
            WifiP2pDevice.INVITED -> "Invited"
            WifiP2pDevice.CONNECTED -> "Connected"
            WifiP2pDevice.FAILED -> "Failed"
            WifiP2pDevice.UNAVAILABLE -> "Unavailable"
            else -> "Unknown"
        }
    }

    override fun toString(): String {
        return "Name = $name\nAddress = $address\nType = $type\nStatus = ${statusDesc()}\nGroupOwner = $groupOwner"
    }
}