package engineer.echo.yi.consumer.ui.bitmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
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
    private val offset = 40f
    private val origin = tpl.toBitmap()
    private val mtx = Matrix()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 36f
        color = Color.parseColor("#88fff143")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            canvas.drawText("Van", origin.width / 2f, origin.height / 2f, paint)
            return
        }
        canvas.drawBitmap(origin, 0f, 0f, null)
        canvas.drawText("origin", offset, offset * 2, paint)

        val diff = 20f
        // 加高度、宽度会有剩余
        val fusionHeight = origin.height + 2 * diff
        val ratio = fusionHeight / origin.height
        val fusionWidth = fusionHeight * origin.width / origin.height
        val diffWidth = (fusionWidth - origin.width - 2 * diff) / 2f

        val py = origin.height + 20f
        // canvas.drawRect(0f, py, fusionWidth, py + fusionHeight, paint)
        mtx.postScale(ratio, ratio)
        mtx.postTranslate(0f, py)
        canvas.drawBitmap(origin, mtx, null)
        // 竖条
        canvas.drawRect(diffWidth, py, diffWidth + diff, py + fusionHeight, paint)
        canvas.drawRect(
            fusionWidth - diffWidth - diff,
            py,
            fusionWidth - diffWidth,
            py + fusionHeight,
            paint
        )
        // 横条
        canvas.drawRect(diffWidth, py, fusionWidth - diffWidth, py + diff, paint)
        canvas.drawRect(
            diffWidth,
            py + fusionHeight - diff,
            fusionWidth - diffWidth,
            py + fusionHeight,
            paint
        )
        mtx.reset()
    }
}