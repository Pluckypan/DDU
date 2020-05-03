package engineer.echo.easylib

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@Suppress("LeakingThis")
abstract class RecyclerViewExposureHelper<T>(
    private val scrollRecyclerView: RecyclerView
) :
    RecyclerView.OnScrollListener() {

    private var hasReportFirstScreen = false
    private var lastResumeTime: Long = 0
    private var lastScrollTime: Long = 0
    private var oldFirstPosition = -1
    private var oldLastPosition = -1
    private val positionList: MutableList<Int> = ArrayList()
    private val tempArray = IntArray(2)

    private val resumeAction = this::recordResume
    private val scrollAction = this::recordScroll

    init {
        scrollRecyclerView.addOnScrollListener(this)
    }

    abstract fun exposureData(positionData: List<T>)

    abstract fun map(position: Int): T

    open fun exposureRecyclerView(): RecyclerView? = scrollRecyclerView

    open fun filter(position: Int): Boolean = true


    private fun recordResume() {
        val rangePosition =
            exposureRecyclerView()?.visibleRange(
                completed = false,
                checkView = false,
                array = tempArray
            ) ?: return
        val firstPosition = rangePosition[0]
        val lastPosition = rangePosition[1]
        if (firstPosition == -1 || lastPosition == -1) return
        positionList.clear()
        for (i in firstPosition..lastPosition) {
            checkView(i)?.let {
                positionList.add(it)
            }
        }
        reportInner(firstPosition, lastPosition)
    }

    private fun reportInner(first: Int, last: Int) {
        positionList.filter {
            filter(it)
        }.map {
            map(it)
        }.let {
            if (it.isNotEmpty()) {
                exposureData(it)
            }
        }
        oldFirstPosition = first
        oldLastPosition = last
    }

    private fun recordScroll() {
        val rangePosition =
            exposureRecyclerView()?.visibleRange(
                completed = false,
                checkView = false,
                array = tempArray
            ) ?: return
        val firstPosition = rangePosition[0]
        val lastPosition = rangePosition[1]
        if (firstPosition == oldFirstPosition && lastPosition == oldLastPosition) {
            return
        }
        positionList.clear()
        //首次&不包含相同项
        if (oldLastPosition == -1 || firstPosition > oldLastPosition || lastPosition < oldFirstPosition) {
            for (i in firstPosition..lastPosition) {
                checkView(i)?.let {
                    positionList.add(it)
                }
            }
        } else {
            //排除相同项
            if (firstPosition < oldFirstPosition) {
                for (i in firstPosition until oldFirstPosition) {
                    checkView(i)?.let {
                        positionList.add(it)
                    }
                }
            }
            if (lastPosition > oldLastPosition) {
                for (i in oldLastPosition + 1..lastPosition) {
                    checkView(i)?.let {
                        positionList.add(it)
                    }
                }
            }
        }
        reportInner(firstPosition, lastPosition)
    }

    private fun checkView(position: Int): Int? {
        return exposureRecyclerView()?.layoutManager?.findViewByPosition(position).let {
            if (it != null) position else null
        }
    }

    private fun handleReportInterval(lastTime: Long, what: Int): Long {
        when (what) {
            FLAG_RESUME -> resumeAction
            FLAG_SCROLL -> scrollAction
            else -> null
        }?.let {
            if (SystemClock.elapsedRealtime() - lastTime < INTERVAL_REPORT) {
                scrollRecyclerView.removeCallbacks(it)
            }
            scrollRecyclerView.postDelayed(it, INTERVAL_REPORT)
        }
        return SystemClock.elapsedRealtime()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            lastScrollTime = handleReportInterval(lastScrollTime, FLAG_SCROLL)
        }
    }

    /**
     * 当第一屏数据请求完调用
     */
    fun reportFirstScreen() {
        if (hasReportFirstScreen) return
        scrollRecyclerView.post {
            onResume()
            hasReportFirstScreen = true
        }
    }

    /**
     * 从后台返回前台调用
     */
    fun onResume() {
        if (exposureRecyclerView()?.adapter?.itemCount ?: 0 > 0) {
            lastResumeTime = handleReportInterval(lastResumeTime, FLAG_RESUME)
        }
    }

    /**
     * Activity#onDestory Fragment#onDestoryView
     */
    fun onDestroy() {
        scrollRecyclerView.removeOnScrollListener(this)
        scrollRecyclerView.removeCallbacks(resumeAction)
        scrollRecyclerView.removeCallbacks(scrollAction)
        oldFirstPosition = -1
        oldLastPosition = -1
        lastResumeTime = 0
        lastScrollTime = 0
    }

    companion object {
        private const val FLAG_SCROLL = 0
        private const val FLAG_RESUME = 1
        private const val INTERVAL_REPORT = 150L
    }
}