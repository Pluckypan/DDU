package engineer.echo.study.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class RoundLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {


    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

    }
    private val mPath = Path()

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}