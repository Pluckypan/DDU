package engineer.echo.whisper.p2p

import android.os.AsyncTask
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.printLine
import engineer.echo.whisper.WhisperConst.TAG
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket

class WifiClientTask(
    private val host: String,
    private val port: Int = 8888,
    private val timeout: Int = 500,
    private val onBegin: (() -> Unit)? = null,
    private val onResult: ((result: Boolean) -> Unit)? = null,
    private val onProgress: ((progress: Int) -> Unit)? = null
) : AsyncTask<InputStream, Int, Boolean>() {

    override fun doInBackground(vararg params: InputStream): Boolean {
        val socket = Socket()
        val buf = ByteArray(1024)
        var len: Int
        try {
            val inputStream = params[0]
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */
            socket.bind(null)
            socket.connect((InetSocketAddress(host, port)), timeout)

            /**
             *  pipe InputStream to the output stream
             * of the socket. This data will be retrieved by the server device.
             */
            val outputStream = socket.getOutputStream()
            val total = inputStream.available()
            var process = 0
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
                process += len
                val pro = if (total <= 0) 0 else (process * 100f / total).toInt()
                publishProgress(pro)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            "Exception %s".formatLog(TAG, e.message)
            return false
        } finally {
            /**
             * Clean up any open sockets when done
             * transferring or if an exception occurred.
             */
            socket.takeIf { it.isConnected }?.apply {
                close()
            }
        }
        return true
    }

    override fun onPreExecute() {
        super.onPreExecute()
        "onPreExecute".printLine(TAG)
        onBegin?.invoke()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values.let {
            if (it.isNotEmpty()) {
                val progress = it[0]
                "onProgressUpdate %d".formatLog(TAG, progress)
                onProgress?.invoke(progress ?: 0)
            }
        }
    }

    /**
     * Start activity that can handle the JPEG image
     */
    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        "onPostExecute %s".formatLog(TAG, result)
        onResult?.invoke(result ?: false)
    }
}