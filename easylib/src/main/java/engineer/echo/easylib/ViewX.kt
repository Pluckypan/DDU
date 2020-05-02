package engineer.echo.easylib

import android.graphics.Rect
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

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

fun GridLayoutManager.lineCount(): Int {
    return currentLine(itemCount - 1)
}

/**
 * @param position adapterPosition
 */
fun GridLayoutManager.spanIndex(position: Int): Int {
    return spanSizeLookup.getSpanIndex(position, spanCount)
}

/**
 * @param pos adapterPosition
 * @return 行号 从1开始
 */
fun GridLayoutManager.currentLine(pos: Int): Int {
    val position = pos + 1
    val t1 = position % spanCount
    val t2 = position / spanCount
    return if (t1 == 0) t2 else t2 + 1
}

/**
 * 获取当前坐标下的 View 对应的 adapterPos
 */
fun RecyclerView.getAdapterPosUnder(x: Float, y: Float): Int {
    val view = findChildViewUnder(x, y) ?: return RecyclerView.NO_POSITION
    return getChildAdapterPosition(view)
}

/**
 * 获取第一个可见 Item adapterPos
 */
fun RecyclerView.findFirstVisibleAdapterPos(completed: Boolean = false): Int {
    return when (val manager = layoutManager ?: return RecyclerView.NO_POSITION) {
        is LinearLayoutManager -> if (completed) manager.findFirstCompletelyVisibleItemPosition() else manager.findFirstVisibleItemPosition()
        is GridLayoutManager -> if (completed) manager.findFirstCompletelyVisibleItemPosition() else manager.findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val positions = IntArray(manager.spanCount)
            val visibleItems =
                if (completed) manager.findFirstCompletelyVisibleItemPositions(positions)
                else manager.findFirstVisibleItemPositions(positions)
            Arrays.sort(visibleItems)
            return visibleItems[0]
        }
        else -> RecyclerView.NO_POSITION
    }
}

/**
 * 获取最后一个可见 Item adapterPos
 */
fun RecyclerView.findLastVisibleAdapterPos(completed: Boolean = false): Int {
    return when (val manager = layoutManager ?: return RecyclerView.NO_POSITION) {
        is LinearLayoutManager -> if (completed) manager.findLastCompletelyVisibleItemPosition() else manager.findLastVisibleItemPosition()
        is GridLayoutManager -> if (completed) manager.findLastCompletelyVisibleItemPosition() else manager.findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val positions = IntArray(manager.spanCount)
            val visibleItems =
                if (completed) manager.findLastCompletelyVisibleItemPositions(positions)
                else manager.findLastVisibleItemPositions(positions)
            Arrays.sort(visibleItems)
            return visibleItems[visibleItems.size - 1]
        }
        else -> RecyclerView.NO_POSITION
    }
}

/**
 * RecyclerView 可见区域
 * @param completed 是否完全可见
 * @param checkView 是否排除遮挡情况
 */
fun RecyclerView.visibleRange(completed: Boolean = false, checkView: Boolean = true): IntArray {
    val result = IntArray(2)
    val firstPos = findFirstVisibleAdapterPos(completed)
    var lastPos = findLastVisibleAdapterPos(completed)
    if (checkView) {
        val rect = Rect()
        layoutManager?.let {
            while (true) {
                val view = it.findViewByPosition(lastPos)
                if (view != null && !view.getGlobalVisibleRect(rect)) {
                    lastPos--
                    continue
                } else {
                    break
                }
            }
        }
    }
    result[0] = firstPos
    result[1] = lastPos
    return result
}