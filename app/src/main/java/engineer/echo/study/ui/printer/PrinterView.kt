package engineer.echo.study.ui.printer

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import engineer.echo.easyprinter.template.TablePrinter

/**
 *  PrinterView.kt
 *  Info: PrinterView.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/23 - 9:21 PM
 *  more about me: http://www.1991th.com
 */

class PrinterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private val painter = TablePrinter()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        painter.apply {
            width = w
            height = w
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        painter.draw(canvas)
    }

    fun getTablePrinter(): TablePrinter {
        return painter
    }
}