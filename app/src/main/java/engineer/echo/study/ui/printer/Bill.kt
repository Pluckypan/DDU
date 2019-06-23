package engineer.echo.study.ui.printer

import android.graphics.Bitmap
import engineer.echo.easyprinter.CommandBox
import engineer.echo.easyprinter.CommandBox.ALIGN_CENTER
import engineer.echo.easyprinter.CommandBox.ALIGN_LEFT
import engineer.echo.easyprinter.CommandBox.PRINT
import engineer.echo.easyprinter.CommandBox.TEXT_BOLD
import engineer.echo.easyprinter.CommandBox.TEXT_BOLD_CANCEL
import engineer.echo.easyprinter.CommandBox.dashLine
import engineer.echo.easyprinter.CommandBox.fontHeight
import engineer.echo.easyprinter.CommandBox.fontSize
import engineer.echo.easyprinter.CommandBox.toPrintByte
import engineer.echo.easyprinter.CommandBox.walkPaper
import java.nio.charset.Charset

data class Bill(
    val logo: Bitmap? = null,
    val title: String,
    val subTitle: String,
    val orderId: String,
    val time: String,
    val phone: String,
    val address: String,
    val tips: String,
    val products: List<Product>
) {

    companion object {
        private val GB2312: Charset = Charset.forName("GB2312")
        fun Bill.toBytes(): ByteArray {
            val logoCode = if (logo != null) {
                ALIGN_CENTER.plus(logo.toPrintByte(120)).plus(walkPaper(2))
            } else {
                byteArrayOf()
            }

            val titleCode =
                ALIGN_CENTER.plus(fontSize(3)).plus(title.toByteArray(GB2312)).plus(walkPaper(1)).plus(PRINT)
            val subTitleCode =
                ALIGN_CENTER.plus(fontSize(2)).plus(subTitle.toByteArray(GB2312)).plus(walkPaper(1)).plus(PRINT)
            val dashCode = ALIGN_LEFT.plus(fontSize(1)).plus(dashLine()).plus(PRINT)
            val orderCode = ALIGN_LEFT.plus("单号: $orderId".toByteArray(GB2312)).plus(PRINT)
            val timeCode = ALIGN_LEFT.plus("时间: $time".toByteArray(GB2312)).plus(PRINT)
            val productTitleCode =
                ALIGN_LEFT.plus(
                    CommandBox.textOneLine(32, GB2312, "菜名", "数量", "单价", "金额").toByteArray(
                        GB2312
                    ).plus(PRINT)
                )
            val prefix = logoCode.plus(titleCode).plus(subTitleCode).plus(dashCode).plus(orderCode).plus(timeCode)
                .plus(dashCode).plus(productTitleCode).plus(dashCode)
            var items = ""
            products.forEach { item ->
                items += CommandBox.textOneLine(
                    32,
                    GB2312,
                    item.name,
                    "x${item.num}",
                    "${item.price}",
                    "${item.price * item.num}"
                ) + "\n"
            }
            val moneyCode = ALIGN_LEFT.plus(TEXT_BOLD).plus(
                CommandBox.textOneLine(32, GB2312, "应收金额:", "", "", "%.2f".format(totalMoney())).toByteArray(
                    GB2312
                ).plus(PRINT)
            )
            val phoneCode = ALIGN_LEFT.plus(TEXT_BOLD_CANCEL).plus("订餐电话: $phone".toByteArray(GB2312)).plus(PRINT)
            val addressCode = ALIGN_LEFT.plus("营业地址: $address".toByteArray(GB2312)).plus(PRINT)
            val tipsCode = ALIGN_CENTER.plus(fontHeight(2)).plus(tips.toByteArray(GB2312)).plus(walkPaper(2)).plus(PRINT)
            return prefix.plus(items.toByteArray(GB2312)).plus(dashCode).plus(moneyCode).plus(phoneCode)
                .plus(addressCode).plus(dashCode).plus(tipsCode)
        }
    }

    fun totalMoney(): Float {
        var result = 0f
        products.forEach {
            result += it.price * it.num
        }
        return result
    }
}