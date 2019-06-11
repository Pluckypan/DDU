package engineer.echo.study.ui.messenger

import android.os.Handler
import android.os.Message
import engineer.echo.easylib.Core.formatLog
import engineer.echo.imessenger.send.SendManager

class SenderService : SendManager() {

    companion object {
        private const val TAG = "SenderService"
    }

    override fun obtainHandler(): Handler {
        return SenderHandler()
    }

    override fun obtainPackageName(): String {
        return "engineer.echo.study.ui.messenger"
    }

    override fun obtainServiceName(): String {
        return "engineer.echo.study.ui.messenger.ReceiverService"
    }

    private class SenderHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            "handleMessage %d %d".formatLog(TAG, msg.what,msg.sendingUid)
        }
    }
}