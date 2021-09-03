package engineer.echo.study.cmpts.apm

import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.util.Printer
import android.util.StringBuilderPrinter
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class AnrWatchDog : Printer {

    private val enable = AtomicBoolean(false)
    private val lastTime = AtomicLong()

    override fun println(x: String) {
        if (DEBUG_MODE.get()) {
            Log.i(TAG, "System >>> $x")
        }
        if (x.contains("Choreographer")) {
            return
        }
        if (enable.get().not()) {
            Looper.getMainLooper().setMessageLogging(null)
            return
        }
        if (x.startsWith(START_PREFIX)) {
            lastTime.set(SystemClock.elapsedRealtime())
        } else if (x.startsWith(END_PREFIX)) {
            val diffTime = SystemClock.elapsedRealtime() - lastTime.get()
            if (diffTime > REPORT_THRESHOLD.get()) {

                Log.i(TAG, "Monitor >>> $x")
            }
        }
    }

    fun startLogging() {
        Looper.getMainLooper().setMessageLogging(this)
        enable.set(true)
    }

    fun endLogging() {
        enable.set(false)
    }

    companion object {

        const val TAG = "AnrWatchDog"

        val DEBUG_MODE = AtomicBoolean(true)
        var REPORT_THRESHOLD = AtomicLong(2000)

        // Looper#169
        private const val START_PREFIX = ">>>>> Dispatching"
        private const val END_PREFIX = "<<<<< Finished"
    }
}