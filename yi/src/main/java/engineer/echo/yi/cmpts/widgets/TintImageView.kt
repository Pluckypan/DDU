package engineer.echo.yi.cmpts.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class TintImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private val paint = Paint().apply {
        color = Color.GREEN
    }

    private val tp = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var index = 0
    private val modes = PorterDuff.Mode.values()

    init {
        isClickable = true
        setOnClickListener {
            index++
            if (index >= modes.size) {
                index = 0
            }
            tp.xfermode = PorterDuffXfermode(modes[index])
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width * 1f, height * 1f, paint)
        canvas.drawText("HE-${index}", 0.5f * width, 0.5f * height, tp)
    }

}