package engineer.echo.study.ui.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.jeremyliao.liveeventbus.LiveEventBus
import engineer.echo.easylib.Core.formatLog
import engineer.echo.imessenger.receive.ReceiveManager

class ReceiverService : ReceiveManager() {

    companion object {

        private const val TAG = "ReceiverService"
        const val KEY_RECEIVE_DATA = "key_for_receive_data"

        private fun postData(key: String, data: Any) {
            LiveEventBus.get().with(key, data.javaClass).post(data)
        }

        fun observeReceive(): LiveEventBus.Observable<Bundle> {
            return LiveEventBus.get().with(KEY_RECEIVE_DATA, Bundle::class.java)
        }

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
            postData(KEY_RECEIVE_DATA, msg.data)
            "handleMessage %d %d".formatLog(TAG, msg.what, msg.sendingUid)
        }
    }
}