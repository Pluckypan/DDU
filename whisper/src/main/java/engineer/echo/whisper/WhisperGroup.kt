package engineer.echo.whisper

class WhisperGroup(
    val networkName: String,
    val passphrase: String? = null,
    val isOwner: Boolean,
    val owner: WhisperDevice,
    val clientList: List<WhisperDevice>
) {

    override fun toString(): String {
        return "Name = $networkName\nPassword = $passphrase\nisOwner = $isOwner\nOwner=$owner\nSize=${clientList.size}"
    }
}