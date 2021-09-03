package engineer.echo.study.cmpts.apm

import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.StringBuilderPrinter

class AnrReporter : HandlerThread(AnrWatchDog.TAG) {

    private val stringBuilder = StringBuilder()


    fun report() {
        Looper.getMainLooper().dump(StringBuilderPrinter(stringBuilder), AnrWatchDog.TAG)
        val str = stringBuilder.toString()
        val stack = Looper.getMainLooper().thread.stackTrace
        val log = Log.getStackTraceString(Throwable())
    }
}