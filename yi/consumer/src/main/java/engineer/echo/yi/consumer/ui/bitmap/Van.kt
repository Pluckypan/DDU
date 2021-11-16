package engineer.echo.yi.consumer.ui.bitmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import engineer.echo.yi.consumer.R

class Van @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val tpl: Drawable = resources.getDrawable(R.drawable.template)
    private val origin = tpl.toBitmap()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 36f
        color = Color.DKGRAY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            canvas.drawText("Van", origin.width / 2f, origin.height / 2f, paint)
            return
        }
        canvas.drawBitmap(origin, 0f, 0f, null)
        canvas.drawText("origin", origin.width / 4f, origin.height / 4f, paint)
    }
}