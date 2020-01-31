package engineer.echo.easyapi

import java.security.MessageDigest
import java.util.*

internal object MD5Tool {

    private val HEX_DIGITS =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    fun getMD5(string: String): String {
        try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(string.toByteArray())
            val messageDigest = digest.digest()
            return toHexString(messageDigest).toLowerCase(Locale.ENGLISH)
        } catch (ignore: Exception) {

        }
        return ""
    }

    private fun toHexString(b: ByteArray): String {
        val sb = StringBuilder(b.size * 2)
        for (b1 in b) {
            sb.append(HEX_DIGITS[(b1.toInt() and 0xf0) ushr 4])
            sb.append(HEX_DIGITS[b1.toInt() and 0x0f])
        }
        return sb.toString()
    }
}
