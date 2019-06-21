package engineer.echo.easyprinter

import android.graphics.Bitmap


/**
 *  CommandBox.kt
 *  Info: 打印机指令盒
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/17 - 11:57 PM
 *  more about me: http://www.1991th.com
 */
object CommandBox {
    /**
     * 复位打印机
     */
    val RESET = byteArrayOf(0x1b, 0x40)
    /**
     * 打印并换行
     */
    val PRINT = byteArrayOf(0x0a)

    /**
     * 靠左
     */
    val ALIGN_LEFT = byteArrayOf(0x1b, 0x61, 0x00)
    /**
     * 居中
     */
    val ALIGN_CENTER = byteArrayOf(0x1b, 0x61, 0x01)
    /**
     * 靠右
     */
    val ALIGN_RIGHT = byteArrayOf(0x1b, 0x61, 0x02)
    /**
     * 选择加粗模式
     */
    val TEXT_BOLD = byteArrayOf(0x1b, 0x45, 0x01)
    /**
     * 取消加粗模式
     */
    val TEXT_BOLD_CANCEL = byteArrayOf(0x1b, 0x45, 0x00)
    /**
     * 字体不放大
     */
    val TEXT_NORMAL_SIZE = byteArrayOf(0x1d, 0x21, 0x00)
    /**
     * 宽高加倍
     */
    val TEXT_BIG_SIZE = byteArrayOf(0x1d, 0x21, 0x11)
    /**
     * 高加倍
     */
    val TEXT_BIG_HEIGHT = byteArrayOf(0x1b, 0x21, 0x10)
    /**
     * 下划线
     */
    val UNDER_LINE = byteArrayOf(0x1b, 0x2d, 2)
    /**
     * 取消下划线
     */
    val UNDER_LINE_CANCEL = byteArrayOf(0x1b, 0x2d, 0)

    /**
     * 作废
     */
    val CNN = byteArrayOf(0x18)

    /**
     * 走纸
     *
     * @param n 行数
     * @return 命令
     */
    fun walkPaper(n: Byte): ByteArray {
        return byteArrayOf(0x1b, 0x64, n)
    }

    /**
     * 设置横向和纵向移动单位
     *
     * @param x 横向移动
     * @param y 纵向移动
     * @return 命令
     */
    fun move(x: Byte, y: Byte): ByteArray {
        return byteArrayOf(0x1d, 0x50, x, y)
    }


    /*--------------- Bitmap --------------*/

    fun Bitmap.toPrintByte(maxWidth: Int = 360): ByteArray {
        return if (width <= maxWidth) {
            this
        } else {
            val dstHeight = (maxWidth * height * 1f / width + 0.5f).toInt()
            Bitmap.createScaledBitmap(this, maxWidth, dstHeight, true)
        }.toPrintBytes()

    }

    private fun Bitmap.pixelByte(x: Int, y: Int): Byte {
        return if (x < width && y < height) {
            val pixel = getPixel(x, y)
            // 取高两位
            val red = pixel and 0x00ff0000 shr 16
            // 取中两位
            val green = pixel and 0x0000ff00 shr 8
            // 取低两位
            val blue = pixel and 0x000000ff
            val gray = 0.29900 * red + 0.58700 * green + 0.11400 * blue
            if (gray < 128) {
                1
            } else {
                0
            }
        } else {
            0
        }
    }

    private fun Bitmap.toPrintBytes(): ByteArray {
        val pHeight = height / 24.0
        val imageSize = Math.ceil(pHeight * (width * 3.0)).toInt()
        val cmdSize = Math.ceil(pHeight * 6).toInt() + 5
        val size = imageSize + cmdSize
        val totalBuffer = ByteArray(size)
        var k = 0
        // 设置行距为0
        totalBuffer[k++] = 0x1B
        totalBuffer[k++] = 0x33
        totalBuffer[k++] = 0x00
        var j = 0
        while (j < pHeight) {
            // 0x1B 2A 表示图片打印指令
            totalBuffer[k++] = 0x1B
            totalBuffer[k++] = 0x2A
            // m=33时，选择24点密度打印
            totalBuffer[k++] = 33
            // nL
            totalBuffer[k++] = (width % 256).toByte()
            // nH
            totalBuffer[k++] = (width / 256).toByte()
            for (i in 0 until width) {
                for (m in 0..2) {
                    for (n in 0..7) {
                        val b = this.pixelByte(i, j * 24 + m * 8 + n)
                        totalBuffer[k] = (2 * totalBuffer[k] + b).toByte()
                    }
                    k++
                }
            }
            // 换行
            totalBuffer[k++] = 10
            j++
        }
        // 恢复默认行距
        totalBuffer[k++] = 0x1B
        totalBuffer[k++] = 0x32

        val result = ByteArray(k)
        System.arraycopy(totalBuffer, 0, result, 0, k)
        return result
    }
}