package engineer.echo.study.ui.messenger

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import engineer.echo.easylib.Core.formatLog
import engineer.echo.imessenger.receive.ReceiveManager

class ReceiverService : ReceiveManager() {

    companion object {

        private const val TAG = "ReceiverService"

        fun start(context: Context) {
            Intent(context, ReceiverService::class.java).also {
                context.startService(it)
            }
        }

        fun stop(context: Context) {
            Intent(context, ReceiverService::class.java).also {
                context.stopService(it)
            }
        }
    }

    override fun obtainHandler(): Handler {
        return ReceiverHandler()
    }

    private class ReceiverHandler : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            "handleMessage %d %d".formatLog(TAG, msg.what, msg.sendingUid)
        }
    }
}