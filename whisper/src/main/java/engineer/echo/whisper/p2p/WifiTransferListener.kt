package engineer.echo.whisper.p2p

import engineer.echo.whisper.WhisperDevice

interface WifiTransferListener {
    fun onDeviceListChanged(deviceList: List<WhisperDevice>)
    fun onThisDeviceChanged(device: WhisperDevice)
}