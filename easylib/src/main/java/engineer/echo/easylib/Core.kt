package engineer.echo.easylib

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import java.io.File

var SWITCH: Boolean = true

fun String.printLine(tag: String = "EasyLib") {
    printLog(SWITCH, tag, "%s", this)
}

fun String.formatLog(tag: String = "EasyLib", vararg args: Any?) {
    printLog(SWITCH, tag, this, *args)
}

fun String.isInteger(): Boolean {
    if (isEmpty()) return false
    if (isDigitsOnly()) return true
    return replace("-", "").isDigitsOnly()
}

private fun printLog(
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

fun Any.printLog(tag: String = "EasyLib", message: String, vararg args: Any?) {
    printLog(SWITCH, "${this.javaClass.simpleName}-$tag", message, *args)
}


fun String?.isFileExist(): Boolean {
    return !this.isNullOrEmpty() && File(this).exists()
}

/**
 * 返回内存标识（不代表内存地址）
 */
fun Any.memInfo(): Int {
    return System.identityHashCode(this)
}

fun String.toastShort(context: Context) {
    Toast.makeText(context.applicationContext, this, Toast.LENGTH_SHORT).show()
}

fun String.toastLong(context: Context) {
    Toast.makeText(context.applicationContext, this, Toast.LENGTH_LONG).show()
}

fun Application.externalDirectory(): File {
    return externalCacheDir?.parentFile ?: File(
        Environment.getExternalStorageDirectory(),
        "${File.separator}Android${File.separator}data${File.separator}${packageName}"
    )
}

fun Application.externalSubDirectory(folder: String): File {
    return File(externalDirectory(), folder).also {
        if (!it.exists()) {
            it.mkdirs()
        }
    }
}

fun Application.externalCache(): File = externalSubDirectory("cache")
fun Application.externalFiles(): File = externalSubDirectory("files")