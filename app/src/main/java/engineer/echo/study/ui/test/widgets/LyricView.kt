package engineer.echo.study.ui.test.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *  LyricView.kt
 *  Info: 歌词逐字显示控件
 *  Created by Plucky(plucky@echo.engineer) on 2019/7/19 - 2:01 PM
 *  more about me: http://www.1991th.com
 */
class LyricView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}