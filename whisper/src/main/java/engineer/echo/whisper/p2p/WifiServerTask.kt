package engineer.echo.whisper.p2p

import android.os.AsyncTask
import java.net.ServerSocket

/**
 * 服务端只能接收文件
 */
class WifiServerTask(private val action: ((bytes: ByteArray?) -> Unit)? = null) : AsyncTask<Void, Void, ByteArray?>() {

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

    /**
     * Start activity that can handle the JPEG image
     */
    override fun onPostExecute(result: ByteArray?) {
        super.onPostExecute(result)
        action?.invoke(result)
    }
}