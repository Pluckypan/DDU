package engineer.echo.study.ui.printer

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.view.drawToBitmap
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.easyprinter.command.CommandBox.toPrintByte
import engineer.echo.easyprinter.command.ImageCommand.scaleToFit
import engineer.echo.easyprinter.template.TablePrinter
import engineer.echo.study.R

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
    private val logo = resources.getDrawable(R.mipmap.ic_launcher)

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
        logo.setBounds(10, 10, logo.intrinsicWidth, logo.intrinsicHeight)
        logo.draw(canvas)
    }

    fun print(device: BluetoothDevice) {
        this.drawToBitmap().scaleToFit(400).toPrintByte(400).also {
            EasyPrinter.get().startPrintTask(device, it)
        }
    }

    fun getTablePrinter(): TablePrinter {
        return painter
    }
}