package engineer.echo.study.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}