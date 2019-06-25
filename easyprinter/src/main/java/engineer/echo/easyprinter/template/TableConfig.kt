package engineer.echo.easyprinter.template

/**
 *  TableConfig.kt
 *  Info: 绘制列表配置
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/26 - 12:06 AM
 *  more about me: http://www.1991th.com
 */
class TableConfig {
    val headers = arrayListOf<String>()
    val items = arrayListOf<ArrayList<String>>()
    var rotation = 0f
    var title: String = ""
    var orderId: String = ""
    var time: String = ""
    var memo: String = ""
}