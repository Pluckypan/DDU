package engineer.echo.study.ui.ipc.messenger

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.jeremyliao.liveeventbus.LiveEventBus
import engineer.echo.easylib.formatLog
import engineer.echo.imessenger.send.SendManager

class SenderService : SendManager() {

    companion object {
        private const val TAG = "SenderService"
        const val KEY_FOR_REPLY = "key_for_reply_data"

        private fun postData(key: String, data: Any) {
            LiveEventBus.get().with(key, data.javaClass).post(data)
        }

        fun observeReply(): LiveEventBus.Observable<Bundle> {
            return LiveEventBus.get().with(KEY_FOR_REPLY, Bundle::class.java)
        }

        fun register(context: Context) {
            SendManager.register(context, SenderService::class.java)
        }

        fun unregister(context: Context) {
            SendManager.unregister(context)
        }

        fun getSenderManager(): SendManager? {
            return SendManager.getSenderManager()
        }
    }

    override fun obtainHandler(): Handler {
        return SenderHandler()
    }

    override fun obtainPackageName(): String {
        return MessengerFragment.getPackageName().also {
            "obtainPackageName %s".formatLog(TAG, it)
        }
    }

    override fun obtainServiceName(): String {
        return ReceiverService::class.java.name.also {
            "obtainServiceName %s".formatLog(TAG, it)
        }
    }

    private class SenderHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            postData(KEY_FOR_REPLY, msg.data)
            "handleMessage %d %d".formatLog(TAG, msg.what, msg.sendingUid)
        }
    }
}