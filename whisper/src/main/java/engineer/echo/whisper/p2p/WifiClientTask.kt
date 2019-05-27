package engineer.echo.whisper.p2p

import android.os.AsyncTask
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket

class WifiClientTask(
    private val host: String,
    private val port: Int,
    private val timeout: Int = 500,
    private val inputStream: InputStream
) : AsyncTask<Void, Int, Boolean>() {

    override fun doInBackground(vararg params: Void): Boolean {
        val socket = Socket()
        val buf = ByteArray(1024)
        var len: Int
        try {
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
        } catch (e: FileNotFoundException) {
            return false
        } catch (e: IOException) {
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
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values.let {
            if (it.isNotEmpty()) {
                val progress = it[0]
            }
        }
    }

    /**
     * Start activity that can handle the JPEG image
     */
    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }
}