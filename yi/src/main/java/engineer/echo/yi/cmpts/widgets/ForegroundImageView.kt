package engineer.echo.yi.cmpts.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

class ForegroundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ImageView(context, attrs, defStyleAttr) {

    private val forePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(153, 0, 0, 0)
    }
    private val foreRect = RectF()
    var radiusX = 0f
    var radiusY = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isSelected) {
            foreRect.set(0f, 0f, width * 1f, height * 1f)
            canvas?.drawRoundRect(foreRect, radiusX, radiusY, forePaint)
        }
    }
}