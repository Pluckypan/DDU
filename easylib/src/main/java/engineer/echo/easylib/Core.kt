package engineer.echo.easylib

import android.content.Context
import android.graphics.Rect
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import java.io.File

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

fun String?.isFileExist(): Boolean {
    return !this.isNullOrEmpty() && File(this).exists()
}

/**
 * 返回内存标识（不代表内存地址）
 */
fun Any.memInfo(): Int {
    return System.identityHashCode(this)
}

fun ViewGroup.allView(): ArrayList<View> {
    val views = ArrayList<View>()
    this.children.forEach {
        if (it is ViewGroup) {
            views.addAll(it.allView())
        } else {
            views.add(it)
        }
    }
    return views
}

fun ViewGroup.groupEnable(isEnable: Boolean) {
    isEnabled = isEnable
    this.allView().forEach {
        it.isEnabled = isEnable
    }
}

fun View.runOnUiThread(action: Runnable) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        action.run()
    } else {
        post(action)
    }
}

fun View.calcViewScreenLocation(): Rect {
    val location = intArrayOf(0, 0)
    // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
    getLocationOnScreen(location)
    return Rect(location[0], location[1], location[0] + width, location[1] + height)

}

fun View.inTouchInPointView(ev: MotionEvent): Boolean {
    val rect = calcViewScreenLocation()
    return rect.contains(ev.rawX.toInt(), ev.rawY.toInt())
}

fun View.alphaVisible(): Boolean {
    return alpha == 1.0f
}

fun String.toastShort(context: Context) {
    Toast.makeText(context.applicationContext, this, Toast.LENGTH_SHORT).show()
}

fun String.toastLong(context: Context) {
    Toast.makeText(context.applicationContext, this, Toast.LENGTH_LONG).show()
}