package engineer.echo.easyapi.download

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object DownloadHelper {

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
        totalSize: Long,
        bufferSize: Int = 4096,
        listener: ((state: State, progress: Int, msg: String?) -> Unit)? = null
    ) {
        val exception = file.tryCreateFile()
        if (exception != null) {
            listener?.invoke(State.OnFail, 0, exception.message)
            return
        }
        use { input ->
            try {
                BufferedOutputStream(FileOutputStream(file)).use { output ->
                    val buffer = ByteArray(bufferSize)
                    var currentLen = 0L
                    val totalLen = if (totalSize == 0L) input.available() * 1L else totalSize
                    var len: Int
                    while (input.read(buffer).also { len = it } != -1) {
                        output.write(buffer, 0, len)
                        currentLen += len
                        listener?.invoke(
                            State.OnProgress,
                            calculateProgress(currentLen, totalLen),
                            null
                        )
                    }
                    listener?.invoke(State.OnFinish, 100, null)
                }
            } catch (e: Exception) {
                listener?.invoke(State.OnFail, 0, e.message)
            }
        }
    }
}