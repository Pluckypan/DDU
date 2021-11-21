package engineer.echo.yi.consumer.ui.bitmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import kotlin.math.sqrt

class FusionTipsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    // 三角中心距右边的距离
    private val triangleOffset = 54f

    // 三角漏出来的距离
    private val triangleOut = 18f

    // 文字内容部分的圆角
    private val radius = 18f

    // 三角的圆角
    private val triangleRadius = 8f

    // 左右内边距
    private val paddingH = 36

    // TextView 顶部内边距
    private val paddingT = 15 + 20

    // 底部内边距
    private val paddingB = 15
    private val halfTriangleWidth = triangleOut * sqrt(2f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FD3960")
    }

    init {
        setPadding(paddingH, paddingT, paddingH, paddingB)
    }

    override fun onDraw(canvas: Canvas) {
        val rx = width - triangleOffset
        canvas.save()
        canvas.translate(rx, paddingT * 1f)
        canvas.rotate(45f)
        canvas.drawRoundRect(
            -halfTriangleWidth,
            -halfTriangleWidth,
            halfTriangleWidth,
            halfTriangleWidth,
            triangleRadius,
            triangleRadius,
            paint
        )
        canvas.restore()
        canvas.drawRoundRect(0f, triangleOut, width * 1f, height * 1f, radius, radius, paint)
        super.onDraw(canvas)
    }
}