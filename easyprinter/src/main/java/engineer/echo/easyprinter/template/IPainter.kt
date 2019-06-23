package engineer.echo.easyprinter.template

import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import engineer.echo.easyprinter.CommandBox
import engineer.echo.easyprinter.CommandBox.PRINT
import engineer.echo.easyprinter.CommandBox.toPrintByte
import engineer.echo.easyprinter.EasyPrinter

/**
 *  IPainter.kt
 *  Info: IPainter.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/23 - 1:30 PM
 *  more about me: http://www.1991th.com
 */
abstract class IPainter {

    val app = EasyPrinter.get().getConfig().application

    var width: Int = app.resources.displayMetrics.widthPixels
    var height: Int = app.resources.displayMetrics.heightPixels


    abstract fun draw(canvas: Canvas)

    fun toBitmap(): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            draw(canvas)
        }
    }

    fun print(
        device: BluetoothDevice,
        align: Paint.Align = Paint.Align.CENTER,
        maxWidth: Int = 360
    ) {
        val data = when (align) {
            Paint.Align.LEFT -> CommandBox.ALIGN_LEFT
            Paint.Align.RIGHT -> CommandBox.ALIGN_RIGHT
            else -> CommandBox.ALIGN_CENTER
        }.plus(toBitmap().toPrintByte(maxWidth)).plus(PRINT)
        EasyPrinter.get().startPrintTask(device, data)
    }
}