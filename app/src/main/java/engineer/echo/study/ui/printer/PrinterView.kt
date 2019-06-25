package engineer.echo.study.ui.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.core.view.drawToBitmap
import engineer.echo.easylib.clipTransparentEdge
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.easyprinter.command.CommandBox.toPrintByte
import engineer.echo.easyprinter.command.ImageCommand.scaleToFit
import engineer.echo.easyprinter.template.TableConfig
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
            setup(TableConfig().apply {
                title = "高丽酒店"
                orderId = "订单号: 0x00156789"
                time = "2019-06-25 00:00"
                memo = "备注: 少放糖"
                headers.addAll(arrayListOf("商品名称", "尺码", "数量", "单价", "金额"))
                items.addAll(
                    arrayListOf(
                        arrayListOf("Nike", "M", "10", "899", "8990"),
                        arrayListOf("阿迪达斯", "L", "2", "390", "780"),
                        arrayListOf("纽巴伦", "X", "1", "599", "599")
                    )
                )
            })
            width = w
            height = w
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        painter.draw(canvas)
    }

    fun setOrientation(rotation: Float) {
        painter.rotation = rotation
        invalidate()
    }

    fun getOrientation(): Float {
        return painter.rotation
    }

    @SuppressLint("WrongThread")
    fun print(device: BluetoothDevice) {
        this.setBackgroundColor(Color.TRANSPARENT)
        this.drawToBitmap(Bitmap.Config.ARGB_8888)
            .clipTransparentEdge()
            .scaleToFit()
            .toPrintByte()
            .also {
                EasyPrinter.get().startPrintTask(device, it)
                this.setBackgroundColor(Color.WHITE)
            }
    }

    fun getTablePrinter(): TablePrinter {
        return painter
    }
}