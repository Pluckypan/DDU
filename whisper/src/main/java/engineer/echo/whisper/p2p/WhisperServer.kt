package engineer.echo.whisper.p2p

import android.app.IntentService
import android.content.Context
import android.content.Intent
import engineer.echo.easylib.Core.formatLog
import engineer.echo.easylib.Core.printLine
import engineer.echo.whisper.WhisperConst.TAG
import java.net.ServerSocket

/**
 *  WhisperServer.kt
 *  Info: Server Service receive data.
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/7 - 1:42 PM
 *  more about me: http://www.1991th.com
 */
class WhisperServer @JvmOverloads constructor(name: String = "WhisperServer") : IntentService(name) {

    companion object {
        fun start(context: Context) {
            Intent(context, WhisperServer::class.java).also {
                context.startService(it)
            }
        }

        fun stop(context: Context) {
            Intent(context, WhisperServer::class.java).also {
                context.stopService(it)
            }
        }
    }

    override fun onHandleIntent(intent: Intent) {
        try {
            ServerSocket(8888).use {
                /**
                 * Wait for client connections. This call blocks until a
                 * connection is accepted from a client.
                 */
                val client = it.accept()
                /**
                 * If this code is reached, a client has connected and transferred data
                 * read bytes from InputStream
                 */
                val input = client.getInputStream()
                it.close()
                val result = input.readBytes()
                "onHandleIntent %s".formatLog(TAG, result.size)
                WhisperBroadcastReceiver.sendData(this, result)
                input.close()
                // start again for receive
                start(this)
            }
        } catch (e: Exception) {
            "onHandleIntent error=%s".formatLog(TAG, e.message)
        }
    }

    override fun onCreate() {
        super.onCreate()
        "onCreate".printLine(TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".printLine(TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "onStartCommand %d %d".formatLog(TAG, flags, startId)
        return super.onStartCommand(intent, flags, startId)
    }
}