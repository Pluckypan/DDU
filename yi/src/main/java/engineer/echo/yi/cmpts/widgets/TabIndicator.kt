package engineer.echo.yi.cmpts.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorRes
import androidx.viewpager.widget.ViewPager
import java.util.*

class TabIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint: Paint = Paint()
    private val rect: RectF
    private val tempData = PositionData()
    private var positionList: List<PositionData>? = null
    private val startInterpolator: Interpolator
    private val endInterpolator: Interpolator
    private var xOffset = 0
    private val lineHeight = 9f
    private val path = Path()
    private var tabIndicatorListener: TabIndicatorListener? = null
    private var lastState = ViewPager.SCROLL_STATE_IDLE

    private val pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            updateView(position, positionOffset)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (tabIndicatorListener != null) {
                tabIndicatorListener!!.onTabIndicatorIndexChanged(lastState > 0, position)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            lastState = state
        }
    }

    init {
        linePaint.color = Color.parseColor("#444648")
        linePaint.isAntiAlias = true
        rect = RectF()
        positionList = ArrayList()
        startInterpolator = AccelerateInterpolator()
        endInterpolator = DecelerateInterpolator()
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.addRoundRect(rect, lineHeight, lineHeight, Path.Direction.CW)
        canvas.drawPath(path, linePaint)
    }

    fun setViewpager(viewpager: ViewPager, list: List<PositionData>) {
        positionList = list
        viewpager.removeOnPageChangeListener(pageChangeListener)
        viewpager.addOnPageChangeListener(pageChangeListener)
        updateView(viewpager.currentItem, 0f)
    }

    fun setLineColor(@ColorRes colorRes: Int) {
        linePaint.color = resources.getColor(colorRes)
    }

    private fun updateView(position: Int, positionOffset: Float) {
        // 计算锚点位置
        val current = getImitativePositionData(positionList, position)
        val next = getImitativePositionData(positionList, position + 1)

        val leftX = (current.left + xOffset).toFloat()
        val nextLeftX = (next.left + xOffset).toFloat()
        val rightX = (current.right - xOffset).toFloat()
        val nextRightX = (next.right - xOffset).toFloat()

        rect.left = leftX + (nextLeftX - leftX) * startInterpolator.getInterpolation(positionOffset)
        rect.right =
            rightX + (nextRightX - rightX) * endInterpolator.getInterpolation(positionOffset)
        rect.top = 0f
        rect.bottom = lineHeight

        invalidate()
    }

    fun setHorizonalOffset(offset: Int) {
        this.xOffset = offset
        invalidate()
    }

    fun setTabIndicatorListener(tabIndicatorListener: TabIndicatorListener) {
        this.tabIndicatorListener = tabIndicatorListener
    }

    //保存位置参数
    class PositionData {
        var left: Int = 0
        var right: Int = 0

        fun reset() {
            left = 0
            right = 0
        }

        fun width(): Int {
            return right - left
        }
    }

    /**
     * 参考代码 https://github.com/hackware1993/MagicIndicator  FragmentContainerHelper
     *
     * @param positionDataList List<PositionData>
     * @param index            int
     * @return 当前位置信息
    </PositionData> */
    private fun getImitativePositionData(
        positionDataList: List<PositionData>?,
        index: Int
    ): PositionData {
        // 越界后，返回假的PositionData
        if (index >= 0 && index <= positionDataList!!.size - 1) {
            return positionDataList[index]
        } else {
            tempData.reset()
            val referenceData: PositionData
            val offset: Int
            if (index < 0) {
                offset = index
                referenceData = positionDataList!![0]
            } else {
                offset = index - positionDataList!!.size + 1
                referenceData = positionDataList[positionDataList.size - 1]
            }
            tempData.left = referenceData.left + offset * referenceData.width()
            tempData.right = referenceData.right + offset * referenceData.width()
            return tempData
        }
    }

    interface TabIndicatorListener {
        fun onTabIndicatorIndexChanged(byUser: Boolean, index: Int)
    }
}
