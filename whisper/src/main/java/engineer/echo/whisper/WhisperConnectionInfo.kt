package engineer.echo.whisper

import java.net.InetAddress

class WhisperConnectionInfo(
    val groupFormed: Boolean,
    val groupOwner: Boolean,
    val groupAddress: InetAddress
) {
    override fun toString(): String {
        return "Owner = $groupOwner\nAddress = ${groupAddress.hostAddress}"
    }
}