package engineer.echo.whisper.p2p

import engineer.echo.whisper.WhisperConnectionInfo
import engineer.echo.whisper.WhisperDevice

interface WifiTransferListener {
    fun onDeviceListChanged(deviceList: List<WhisperDevice>)
    fun onDeviceInfoChanged(device: WhisperDevice)
    fun onDeviceAvailable(enable: Boolean)
    fun onDeviceConnectionChanged(connected: Boolean)
    fun onDeviceConnectionInfoChanged(info: WhisperConnectionInfo)
}