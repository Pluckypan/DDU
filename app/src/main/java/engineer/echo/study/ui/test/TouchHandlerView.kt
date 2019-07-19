package engineer.echo.study.ui.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

/**
 *  TouchHandlerView.kt
 *  Info: TouchHandlerView.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/7/2 - 6:43 PM
 *  more about me: http://www.1991th.com
 */
class TouchHandlerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FA2855")
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    var startX = 0f
    var endX = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startX = 0f
        endX = w * 1f
    }

    val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(startX, 0f, endX, height * 1f, paint)
    }

    var lastX = 0f
    var offsetX = 0f
    var useLeft = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                offsetX = 0f
                useLeft = lastX < width / 2
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = (event.x - lastX)
                offsetX += dx
                lastX = event.x
                if (useLeft) {
                    startX += dx
                    if (startX < 0) startX = 0f
                    if (startX > width) startX = width * 1f
                } else {
                    endX += dx
                    if (endX < 0) endX = 0f
                    if (endX > width) endX = width * 1f
                }
                invalidate()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {

            }
        }
        return super.onTouchEvent(event)
    }
}