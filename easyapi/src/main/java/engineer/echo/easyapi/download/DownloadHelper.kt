package engineer.echo.easyapi.download

import android.text.TextUtils
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object DownloadHelper {

    fun deleteIfExist(path: String?): Boolean {
        if (path == null) return false
        return File(path).let {
            if (it.exists()) it.delete() else false
        }
    }

    fun Call<*>.resumeBytes(): Long {
        request().header("RANGE")?.replace("bytes=", "")?.replace("-", "")?.let {
            return if (TextUtils.isDigitsOnly(it)) it.toLong() else 0
        }
        return 0
    }

    fun File.tryCreateFile(): Exception? {
        if (!exists()) {
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            try {
                createNewFile()
            } catch (e: Exception) {
                return e
            }
        }
        return null
    }

    fun calculateProgress(current: Long, total: Long): Int {
        if (total > 0 && current >= 0 && current <= total) return (current * 100f / total).toInt()
        return 0
    }

    /**
     * @param file the file Write to
     * @param listener write state
     */
    fun InputStream.writeToFile(
        file: File,
        append: Boolean = false,
        bufferSize: Int = 4096,
        listener: ((state: State, current: Long, msg: String?) -> Unit)? = null
    ) {
        val exception = file.tryCreateFile()
        if (exception != null) {
            listener?.invoke(State.OnFail, 0, exception.message)
            return
        }
        use { input ->
            try {
                FileOutputStream(file, append).use { output ->
                    val buffer = ByteArray(bufferSize)
                    var currentLen = 0L
                    var len: Int
                    while (input.read(buffer).also { len = it } != -1) {
                        output.write(buffer, 0, len)
                        currentLen += len
                        listener?.invoke(
                            State.OnProgress,
                            currentLen,
                            null
                        )
                    }
                    listener?.invoke(State.OnFinish, currentLen, null)
                }
            } catch (e: Exception) {
                file.delete()
                listener?.invoke(State.OnFail, 0, e.message)
            }
        }
    }
}