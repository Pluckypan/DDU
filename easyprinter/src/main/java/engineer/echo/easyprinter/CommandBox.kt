package engineer.echo.easyprinter

import android.graphics.Bitmap
import java.nio.charset.Charset


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

    val OPEN = byteArrayOf(0x10, 0x14, 0x00, 0x00)

    /**
     * 作废
     */
    val CNN = 0x18
    /**
     * 空格 32
     */
    val SP: Byte = 0x1F
    /**
     * 横向列表 9
     */
    val HT: Byte = 0x09

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

    /**
     * 打印制表符 TAB
     * @param num 个数
     */
    fun printTab(num: Int): ByteArray {
        val result = ByteArray(num)
        for (i in 0 until num) {
            // "\t"
            result[i] = HT
        }
        return result
    }

    /**
     * 放大字体到默认字体大小的N倍
     * @param scale 放大倍数
     */
    fun fontSize(scale: Int): ByteArray {
        val realSize: Byte = when (scale) {
            1 -> 0
            2 -> 17
            3 -> 34
            4 -> 51
            5 -> 68
            6 -> 85
            7 -> 102
            8 -> 119
            else -> 0
        }
        val result = ByteArray(3)
        result[0] = 29
        result[1] = 33
        result[2] = realSize
        return result
    }

    /**
     * 设置左边距
     * 空白量=(nL+nH×256)×0.125毫米
     */
    fun marginLeft(nL: Byte = 0, nH: Byte = 0): ByteArray {
        val result = ByteArray(4)
        result[0] = 0x1d
        result[1] = 0x4c
        result[2] = nL
        result[3] = nH
        return result
    }

    /**
     * 字间距
     */
    fun wordGap(gap: Byte): ByteArray {
        val result = ByteArray(3)
        result[0] = 0x1b
        result[1] = 0x20
        result[2] = gap
        return result
    }

    /**
     * 行间距
     */
    fun lineGap(gap: Byte): ByteArray {
        val result = ByteArray(3)
        result[0] = 0x1b
        result[1] = 0x33
        result[2] = gap
        return result
    }

    /**
     * 打印区域宽度=(nL+nH×256)×0.125毫米
     */
    fun printAreaWidth(nL: Byte = 76, nH: Byte = 2): ByteArray {
        val result = ByteArray(4)
        result[0] = 0x1d
        result[1] = 0x57
        result[2] = nL
        result[3] = nH
        return result
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

    /* -------------- 条形码 ----------------*/
    enum class BarcodeType(val code: Byte) {
        // European Article Number(EAN) 690123456789(690表示为日用品 1234表示生产商代码 56789表示产品代码)
        EAN13(0x43),
        // 支持44个有效输入 数字 0~9 26个大写字母 -,.,空格,$,/,+,%
        CODE39(0x45),
        // 运输包装码 仅数字
        ITF(0x46),
        // 分ABC "{A" 表示大写字母 "{B" 表示所有字母，数字，符号 "{C" 表示数字，可以表示 00 - 99 的范围
        CODE128(0x49)
    }

    /**
     * @param width [2,6]
     * @param height [1,255]
     * @param hriFontPos  [0,3] [48,51] 0,48不打印 1,49条码上方 2,50条码下方 3,51条码上、下方都打印
     */
    fun String.toBarcode(
        barType: BarcodeType = BarcodeType.CODE128,
        charset: Charset = Charsets.UTF_8,
        width: Int = 2,
        height: Int = 162,
        hriFontPos: Int = 2
    ): ByteArray {
        val content = when (barType) {
            CommandBox.BarcodeType.CODE128 -> {
                if (this.startsWith("{A")
                    || this.startsWith("{B")
                    || this.startsWith("{C")
                ) {
                    this
                } else {
                    "{B$this"
                }
            }
            else -> this
        }
        // 位置指令
        val positionCode = byteArrayOf(0x1d, 0x48, hriFontPos.toByte())
        // 宽度指令
        val widthCode = byteArrayOf(0x1d, 0x77, width.toByte())
        // 高度指令
        val heightCode = byteArrayOf(0x1d, 0x68, height.toByte())
        // 类型指令
        val typeCode = byteArrayOf(0x1d, 0x6b, barType.code, content.length.toByte())
        // 打印指令
        val printCode = byteArrayOf(10, 0)

        return positionCode.plus(widthCode).plus(heightCode).plus(typeCode).plus(content.toByteArray(charset))
            .plus(printCode)
    }

    /**
     * 爱普森QRCode指令
     */
    fun String.epsonQrcode(charset: Charset = Charsets.UTF_8): ByteArray {
        val totalLen = this.length + 3
        val pL = (totalLen % 256).toByte()
        val pH = (totalLen / 256).toByte()

        val modelQR = byteArrayOf(0x1d, 0x28, 0x6b, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00)
        val sizeQR = byteArrayOf(0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x43, 0x08)
        val errorQR = byteArrayOf(0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x45, 0x31)
        val storeQR = byteArrayOf(0x1d, 0x28, 0x6b, pL, pH, 0x31, 0x50, 0x30)
        val printQR = byteArrayOf(0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x51, 0x30)

        return modelQR.plus(sizeQR).plus(errorQR).plus(storeQR).plus(toByteArray(charset)).plus(printQR)
    }
}

