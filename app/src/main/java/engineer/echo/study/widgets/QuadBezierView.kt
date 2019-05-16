package engineer.echo.study.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *  QuadBezierView.kt
 *  Info: 二阶贝舍尔曲线
 *  Created by Plucky(plucky@echo.engineer) on 2019/5/16 - 5:50 PM
 *  more about me: http://www.1991th.com
 */

class QuadBezierView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {


    private val mCtrlPoint = PointF(0f, 0f)
    private val mCtrlPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    private val mBezierPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    private val mPath = Path()
    private val mMeasure = PathMeasure(mPath, false)
    private val mPos = FloatArray(2)
    private val mTan = FloatArray(2)
    private var mProgress = 0f

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        val cx = width / 4f
        val cy = height * 3 / 4f
        canvas.drawText("$cx,$cy", cx, cy - 20, mCtrlPaint)
        val ex = width * 3 / 4f
        val ey = height / 4f
        canvas.drawText("$ex,$ey", ex, ey - 20, mCtrlPaint)
        // 画坐标轴
        canvas.drawLine(0f, cy, width * 1f, cy, mLinePaint)
        canvas.drawLine(cx, 0f, cx, height * 1f, mLinePaint)
        // 画控制点
        canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, 16f, mCtrlPaint)
        // 开始点
        canvas.drawCircle(cx, cy, 8f, mBezierPaint)
        // 结束点
        canvas.drawCircle(ex, ey, 8f, mBezierPaint)
        // 画二阶贝舍尔曲线
        mPath.reset()
        mPath.moveTo(cx, cy)
        mPath.quadTo(mCtrlPoint.x, mCtrlPoint.y, ex, ey)
        canvas.drawPath(mPath, mBezierPaint)
        // 画切线
        canvas.drawLine(cx, cy, mCtrlPoint.x, mCtrlPoint.y, mCtrlPaint)
        canvas.drawLine(ex, ey, mCtrlPoint.x, mCtrlPoint.y, mCtrlPaint)
        // Path动态点计算
        mMeasure.setPath(mPath, false)
        mMeasure.getPosTan(mMeasure.length * mProgress, mPos, mTan)
        val mx = mPos[0]
        val my = mPos[1]
        val point = String.format("[归一化:%.2f]", mProgress)
        canvas.drawText(point, cx / 2, cy / 2, mCtrlPaint)
        // 画动态点
        canvas.drawCircle(mx, my, 12f, mCtrlPaint)
        val movePoint = String.format("%.1f,%.1f", mx, my)
        canvas.drawText(movePoint, mx, my - 20, mCtrlPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCtrlPoint.set(width * 3 / 4f, height * 3 / 4f)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                if (event.x > (width / 4f)) {
                    mCtrlPoint.set(event.x, event.y)
                } else {
                    val seekBar = height - 2 * 40f
                    val ey = if (event.y < 40) 0f else if (event.y > height - 40) height - 40f else event.y
                    mProgress = (seekBar - ey) / seekBar
                    mProgress = Math.max(mProgress, 0f)
                    mProgress = Math.min(mProgress, 1f)
                }

                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}