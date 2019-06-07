package engineer.echo.easylib

import android.util.Log

object Core {

    var SWITCH: Boolean = true

    fun String.printLine(tag: String = "EasyLib") {
        printLog(SWITCH, tag, "%s", this)
    }

    fun String.formatLog(
        tag: String = "EasyLib",
        vararg args: Any?
    ) {
        printLog(SWITCH, tag, this, *args)
    }

    fun printLog(
        switch: Boolean = SWITCH,
        tag: String = "EasyLib",
        format: String,
        vararg args: Any?
    ) {
        if (switch) {
            format.format(*args).let {
                Log.d(tag, it)
            }
        }
    }
}