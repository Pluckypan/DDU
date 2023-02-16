package engineer.echo.easylib

import android.os.Handler
import android.os.Message
import android.util.Log

class ActivityThreadHandlerCallback(activityThreadHandler: Handler) : Handler.Callback {
    private val activityThreadHandler: Handler

    init {
        this.activityThreadHandler = activityThreadHandler
    }

    override fun handleMessage(msg: Message): Boolean {
        Log.i("Plucky", "handleMessage $msg")
        activityThreadHandler.handleMessage(msg)
        return true
    }
}