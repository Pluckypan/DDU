package engineer.echo.easyprinter

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
}