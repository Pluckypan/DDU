package engineer.echo.easyprinter.entity

/**
 *  DiscoveryState.kt
 *  Info: 扫描状态
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 1:01 PM
 *  more about me: http://www.1991th.com
 */
enum class DiscoveryState(val state: String) {
    Start("StartDiscovery"),
    Ing("Discovering"),
    Finish("onFinishDiscovery")
}