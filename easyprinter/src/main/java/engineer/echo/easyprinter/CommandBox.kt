package engineer.echo.easyprinter

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

    /**
     * UPC-A类型：国际商品条码。共 12 位数字组成，最后一位是校验码，根据前 11 位数字计算得出，相当于数字0开头的EAN-13码。主要为美国和加拿大使用。而UPC-E类型是其缩短的一种形式。
     * EAN-13类型：国际商品条码。是当今世界上使用最广的商品条码。共 13 位数字组成，最后一位是校验码，根据前 12 位数字计算得出。是当今世界上广为使用的商品条码，已成为电子数据交换（EDI）的基础。而EAN-8类型是其缩短的一种形式。
     * NW-7类型:是一种条、空均表示信息的非连续、可变长度、双向自检的条码，可表示数字0-9、字母A-D及特殊字符（+、—、$、：、/、·）,其中ABCD仅作为启始符和终止符，并可任意组合。主要用于医疗卫生、图书报刊、物资等领域的自动认别。
     * Code 39类型：是一种条、空均表示信息的非连续型条码，它可表示数字0-9、字母A-Z和八个控制字符（-、空格、/、＄、+、%、·、*）等44个字符，主要用于工业、图书及票证的自动化管理，目前使用极为广泛。而Code 39 Extended是39码的全ASCII形式。使用2个字符可以将128个ASCII全部字符集进行编码。
     * Code 128类型：对全部128个字符进行编码。通过起始字符选择不同的代码集。A\B\C代表不同的数据范围，A：大写字母+数字，B：大小写字母+数字，C：为偶数纯数字编码，而Auto是根据数据自动选择起始符进行最短编码。UCC/EAN是在Code 128的基础上扩展的应用标识条码、能标识贸易单元中需表示的信息，如产品批号、数量、生产日期等。SCC和SSCC 为细分的AI标识符条码。
     * ITF -14类型：是Interleaved 2 of 5 类型的一种规范应用，条形码长度没有限制，用5个黑条和5个白条表示2位数字。比其它的 2 of 5 相比密度要高，但是数据必须为偶位数。条码四周的边框为支撑条，其作用为保护条码的识别区域。多使用于UPC标准物流符号及日本的标准物流符号等包装箱印刷中。
     */
    enum class BarcodeType(val byte: Byte) {
        UPC_A(0),
        UPC_E(1),
        EAN13(2),
        EAN8(3),
        CODE39(4),
        ITF(5),
        NW7(6),
        CODE93(72),
        CODE128(73)
    }

    fun String.toBarcode(
        type: BarcodeType = CommandBox.BarcodeType.NW7,
        charset: Charset = Charsets.UTF_8
    ): ByteArray {
        val barcodeBytes = this.toByteArray(charset)
        val result = ByteArray(3 + barcodeBytes.size + 1)
        result[0] = 29
        result[1] = 107
        result[2] = type.byte
        var idx = 3
        for (b in barcodeBytes) {
            result[idx] = b
            idx++
        }
        result[idx] = 0
        return result
    }

}