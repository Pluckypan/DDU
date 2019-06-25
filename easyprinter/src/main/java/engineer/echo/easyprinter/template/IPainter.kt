package engineer.echo.easyprinter.template

import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import engineer.echo.easylib.clipTransparentEdge
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.easyprinter.command.CommandBox
import engineer.echo.easyprinter.command.CommandBox.PRINT
import engineer.echo.easyprinter.command.CommandBox.toPrintByte
import engineer.echo.easyprinter.command.ImageCommand.scaleToFit

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

    fun toBitmap(clipTransParent: Boolean = true): Bitmap {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).applyCanvas(::draw).also {
            return if (clipTransParent) it.clipTransparentEdge() else it
        }
    }

    fun print(
        device: BluetoothDevice,
        align: Paint.Align = Paint.Align.CENTER,
        maxWidth: Int = 360,
        clipTransParent: Boolean = true
    ) {
        val data = when (align) {
            Paint.Align.LEFT -> CommandBox.ALIGN_LEFT
            Paint.Align.RIGHT -> CommandBox.ALIGN_RIGHT
            else -> CommandBox.ALIGN_CENTER
        }.plus(toBitmap(clipTransParent).scaleToFit(maxWidth).toPrintByte(maxWidth)).plus(PRINT)
        EasyPrinter.get().startPrintTask(device, data)
    }
}