package engineer.echo.study.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 *  ManualQuadBezierView.kt
 *  Info: 定点二阶贝舍尔曲线
 *  Created by Plucky(plucky@echo.engineer) on 2019/5/20 - 10:01 AM
 *  more about me: http://www.1991th.com
 */

class ManualQuadBezierView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes), ValueAnimator.AnimatorUpdateListener {


    private val mStartPoint = PointF(0f, 0f)
    private val mEndPoint = PointF(0f, 0f)
    private val mCtrlPoint = PointF(0f, 0f)

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 32f
        textAlign = Paint.Align.CENTER
        letterSpacing = 0.02f
    }
    private val mCoordPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 4f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(15f, 5f), 0f)
    }

    private val mMovePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
    }
    private val mPos = FloatArray(2)
    private val mTan = FloatArray(2)

    private val mPath = Path()
    private val mMovePath = Path()
    private val mMeasure = PathMeasure(mPath, false)
    private var mProgress = 0.5f
    private val mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        interpolator = LinearInterpolator()
        duration = 1500
    }

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        val ox = width / 4f
        val oy = height * 3 / 4f
        val ex = width * 3 / 4f
        val ey = height / 4f
        // 画坐标轴
        canvas.drawLine(0f, oy, width * 1f, oy, mCoordPaint)
        canvas.drawLine(ox, 0f, ox, height * 1f, mCoordPaint)
        // 原点
        canvas.drawCircle(ox, oy, 8f, mCoordPaint)
        canvas.drawText("[0f,0f]", ox - 50, oy + 50, mTextPaint)
        // 极值点
        canvas.drawCircle(ex, ey, 8f, mCoordPaint)
        canvas.drawText("[60000f,1.0f]", ex, ey - 30, mTextPaint)
        canvas.drawLine(ox, ey, ex + 25f, ey, mCoordPaint)
        canvas.drawLine(ex, oy, ex, ey - 25f, mCoordPaint)

        // 画控制点
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 12f, mPointPaint)
        canvas.drawCircle(mEndPoint.x, mEndPoint.y, 12f, mPointPaint)
        canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, 12f, mPointPaint)
        // 画二阶贝舍尔曲线(虚线)
        mPath.reset()
        mPath.moveTo(mStartPoint.x, mStartPoint.y)
        mPath.quadTo(mCtrlPoint.x, mCtrlPoint.y, mEndPoint.x, mEndPoint.y)
        canvas.drawPath(mPath, mPointPaint)
        // 动态Path
        mMeasure.setPath(mPath, false)
        mMovePath.reset()
        val distance = mMeasure.length * mProgress
        mMeasure.getSegment(0f, distance, mMovePath, true)
        canvas.drawPath(mMovePath, mMovePaint)
        // 画切线
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mCtrlPoint.x, mCtrlPoint.y, mTextPaint)
        mMeasure.setPath(mMovePath, false)
        mMeasure.getPosTan(distance, mPos, mTan)
        canvas.drawLine(mPos[0], mPos[1], mCtrlPoint.x, mCtrlPoint.y, mTextPaint)
        // 画参数说明
        canvas.drawText("[x:时间] - [y:音量]", ox / 2, oy / 2, mTextPaint)
        val px = (mPos[0] - ox) * 60000f / (width / 2f)
        val py = (oy - mPos[1]) / (height / 2f)
        val point = String.format(" %.2f - %.2f ", px, py)
        canvas.drawText(point, ox / 2, oy / 2 + 40f, mTextPaint)
    }

    var startX: Float = 0f
    var startY: Float = 0f
    var stopX: Float = 60000f
    var stopY: Float = 1.0f
    var ctrlX: Float = 60000f
    var ctrlY: Float = 0f
    fun start(
        startX: Float = 0f, startY: Float = 0f,
        stopX: Float = 60000f, stopY: Float = 1.0f,
        ctrlX: Float = 60000f, ctrlY: Float = 0f
    ) {
        this.startX = startX
        this.startY = startY
        this.stopX = stopX
        this.stopY = stopY
        this.ctrlX = ctrlX
        this.ctrlY = ctrlY
        // 必须要界面测绘完成
        if (width == 0 || height == 0) return
        val ox = width / 4f
        val oy = height * 3 / 4f
        val axisX = width / 2f
        val axisY = height / 2f

        if (mAnimator.isRunning) {
            mAnimator.cancel()
        }
        val sx = startX * axisX / 60000f + ox
        val sy = oy - startY * axisY / 1f
        mStartPoint.set(sx, sy)
        val ex = stopX * axisX / 60000f + ox
        val ey = oy - stopY * axisY / 1f
        mEndPoint.set(ex, ey)
        val cx = ctrlX * axisX / 60000f + ox
        val cy = oy - ctrlY * axisY / 1f
        mCtrlPoint.set(cx, cy)

        mAnimator.start()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAnimator.addUpdateListener(this)
        post {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        mAnimator.removeUpdateListener(this)
        if (mAnimator.isRunning) {
            mAnimator.cancel()
        }
        super.onDetachedFromWindow()

    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mProgress = animation.animatedFraction
        invalidate()
    }
}