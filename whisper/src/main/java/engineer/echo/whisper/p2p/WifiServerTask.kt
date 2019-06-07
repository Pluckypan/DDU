package engineer.echo.whisper.p2p

import android.os.AsyncTask
import engineer.echo.easylib.Core.formatLog
import engineer.echo.easylib.Core.printLine
import engineer.echo.whisper.WhisperConst.TAG
import java.net.ServerSocket

/**
 * 服务端只能接收文件
 */
class WifiServerTask(
    private val onBegin: (() -> Unit)? = null,
    private val onResult: ((bytes: ByteArray?) -> Unit)? = null
) : AsyncTask<Void, Void, ByteArray?>() {

    override fun doInBackground(vararg params: Void): ByteArray? {
        /**
         * Create a server socket.
         */
        val serverSocket = ServerSocket(8888)
        return serverSocket.use {
            /**
             * Wait for client connections. This call blocks until a
             * connection is accepted from a client.
             */
            val client = serverSocket.accept()
            /**
             * If this code is reached, a client has connected and transferred data
             * read bytes from InputStream
             */
            val input = client.getInputStream()
            serverSocket.close()
            input.readBytes()
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        "onPreExecute".printLine()
        onBegin?.invoke()
    }

    /**
     * Start activity that can handle the JPEG image
     */
    override fun onPostExecute(result: ByteArray?) {
        super.onPostExecute(result)
        "onPostExecute %s".formatLog(TAG, (result?.size ?: 0))
        onResult?.invoke(result)
    }
}