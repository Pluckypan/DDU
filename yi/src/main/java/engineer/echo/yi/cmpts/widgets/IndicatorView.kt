package engineer.echo.yi.cmpts.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        alpha = 40
    }
    private val selectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        alpha = 30
    }
    private val bgRectF = RectF()
    private var autoSize = true
    private var distance: Float = 40f
    private var radius: Float = 10f
    var size: Int = 0
        set(value) {
            field = if (value < 0) 0 else value
            if (autoSize && distance == 0f && size > 0) {
                calculateDis(width)
            }
        }
    var currentIndex: Int = 0
        set(value) {
            field = if (value > size - 1) size - 1 else if (value < 0) 0 else value
            invalidate()
        }

    fun setup(
        index: Int = 0,
        size: Int = 0,
        radius: Float = 10f,
        distance: Float = 40f,
        autoSize: Boolean = true
    ) {
        this.size = size
        this.radius = radius
        this.distance = distance
        this.autoSize = autoSize
        this.currentIndex = index
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (autoSize) {
            radius = h / 4f
            calculateDis(w)
        }
    }

    private fun calculateDis(w: Int) {
        distance = if (size == 0) 0f else (w - 2 * radius) / size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        if (!isInEditMode) {
            if (size > 0) {
                // 背景
                val hw = (size * distance + 2 * radius) / 2f
                bgRectF.set(cx - hw, 0f, cx + hw, height * 1f)
                canvas.drawRoundRect(bgRectF, cy, cy, bgPaint)
                // 第一个点的位置
                val ix = cx - (size - 1) * distance / 2f
                // 画指示点
                for (i in 0 until size) {
                    canvas.drawCircle(ix + i * distance, cy, radius, normalPaint)
                }
                // 画选中的点
                canvas.drawCircle(ix + currentIndex * distance, cy, radius, selectPaint)
            }
        } else {
            canvas.drawCircle(cx, cy, radius, selectPaint)
        }
    }
}