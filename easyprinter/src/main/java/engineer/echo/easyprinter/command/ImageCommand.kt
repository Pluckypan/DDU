package engineer.echo.easyprinter.command

import android.graphics.Bitmap
import android.graphics.Color

/**
 *  ImageCommand.kt
 *  Info: 打印Bitmap相关的指令
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/24 - 11:54 PM
 *  more about me: http://www.1991th.com
 */
object ImageCommand {

    /**
     * 将Bitmap转换为POS机可识别的指令
     */
    fun covert(bitmap: Bitmap, maxWidth: Int = 360, mode: Int = 0): ByteArray {
        if (bitmap.width <= maxWidth) {
            bitmap
        } else {
            bitmap.scaleToFit(maxWidth)
        }.also {
            return it.toPrintBytes(mode).plus(CommandBox.walkPaper(2))
        }

    }

    /**
     * 缩放到合适宽度
     * @param fitWidth 最大宽度
     */
    fun Bitmap.scaleToFit(fitWidth: Int = 360): Bitmap {
        return if (width <= fitWidth) {
            this
        } else {
            val dstHeight = (fitWidth * height * 1f / width + 0.5f).toInt()
            Bitmap.createScaledBitmap(this, fitWidth, dstHeight, true)
        }
    }

    /**
     * 图片二值化
     * @param loopWidth 打印宽度
     * @param threshold 判断黑白的条件
     */
    fun Bitmap.toGray(
        loopWidth: Int,
        threshold: Int = 128
    ): ByteArray {
        val bytes = ArrayList<Byte>()
        for (y in 0 until this.height) {
            for (x in 0 until loopWidth) {
                val intensity = if (x < this.width) {
                    val pixel = this.getPixel(x, y)
                    255 - ((Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3)
                } else {
                    0
                }
                if (intensity > threshold) {
                    bytes.add(1)
                } else {
                    bytes.add(0)
                }
            }
        }
        return bytes.toByteArray()
    }

    private fun Bitmap.toPrintBytes(mode: Int = 0): ByteArray {
        var printWidth = 8 - (width % 8)
        if (printWidth == 8) {
            printWidth = width
        } else {
            printWidth += width
        }
        return toGray(printWidth)
            .toPrintBytes(printWidth, mode)
    }

    private val P0 = intArrayOf(0, 0x80)
    private val P1 = intArrayOf(0, 0x40)
    private val P2 = intArrayOf(0, 0x20)
    private val P3 = intArrayOf(0, 0x10)
    private val P4 = intArrayOf(0, 0x08)
    private val P5 = intArrayOf(0, 0x04)
    private val P6 = intArrayOf(0, 0x02)

    private fun ByteArray.toPrintBytes(width: Int, mode: Int): ByteArray {
        val height = this.size / width
        val bytesInLine = width / 8
        val data = ByteArray(height * (8 + bytesInLine))
        var offset: Int
        var k = 0
        for (i in 0 until height) {
            offset = i * (8 + bytesInLine)
            data[offset + 0] = 0x1d
            data[offset + 1] = 0x76
            data[offset + 2] = 0x30
            data[offset + 3] = (mode and 0x01).toByte()
            data[offset + 4] = (bytesInLine % 0x100).toByte()
            data[offset + 5] = (bytesInLine / 0x100).toByte()
            data[offset + 6] = 0x01
            data[offset + 7] = 0x00
            for (j in 0 until bytesInLine) {
                data[offset + 8 + j] =
                    (P0[this[k].toInt()]
                            + P1[this[k + 1].toInt()]
                            + P2[this[k + 2].toInt()]
                            + P3[this[k + 3].toInt()]
                            + P4[this[k + 4].toInt()]
                            + P5[this[k + 5].toInt()]
                            + P6[this[k + 6].toInt()]
                            + this[k + 7].toInt()).toByte()
                k += 8
            }
        }

        return data
    }

}