package engineer.echo.easyapi.download

import android.text.TextUtils
import androidx.annotation.WorkerThread
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.MD5Tool
import okhttp3.Request
import retrofit2.Call
import retrofit2.http.Url
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import okhttp3.Call as OkCall

object DownloadHelper {

    fun deleteIfExist(path: String?): Boolean {
        if (path == null) return false
        return File(path).let {
            if (it.exists()) it.delete() else false
        }
    }

    fun invalidSavedPath(): Exception = Exception("-1", Throwable("invalid savedPath"))
    fun invalidFileRange(): Exception = Exception("-2", Throwable("invalid file range"))
    fun downloadTaskExist(): Exception = Exception("-3", Throwable("duplicate download task"))

    fun Call<*>.resumeBytes(): Long {
        request().header("RANGE")?.replace("bytes=", "")?.replace("-", "")?.let {
            return if (TextUtils.isDigitsOnly(it)) it.toLong() else 0
        }
        return 0
    }

    @WorkerThread
    fun getFileLength(@Url url: String): Long {
        var result = 0L
        EasyApi.getClient()?.newCall(Request.Builder().url(url).build())?.execute()?.use {
            result = it.body()?.contentLength() ?: 0L
        }
        return result
    }

    fun Call<*>.urlAndPath(): Pair<String, String?> {
        val url = request().url().url().toString()
        val path = request().tag(String::class.java)
        return Pair(url, path)
    }

    fun downloadId(@Url url: String, path: String): String =
        MD5Tool.getMD5("[EasyApi][$url][$path]")

    fun OkCall.okDownloadId(): String {
        val url = request().url().url().toString()
        val path = request().tag(String::class.java)
        return downloadId(url, path ?: "")
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
        offset: Long = 0,
        bufferSize: Int = 4096,
        listener: ((state: State, current: Long, msg: String?) -> Unit)? = null
    ) {
        val exception = file.tryCreateFile()
        if (exception != null) {
            EasyApi.printLog("writeToFile createFile error = %s", exception.message)
            listener?.invoke(State.OnFail, 0, exception.message)
            return
        }
        use { input ->
            try {
                RandomAccessFile(file, "rw").use { output ->
                    if (offset > 0) {
                        output.seek(offset)
                    }
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
                EasyApi.printLog("writeToFile error = %s", e.message)
                listener?.invoke(State.OnFail, 0, e.message)
            }
        }
    }
}