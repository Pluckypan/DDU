package engineer.echo.easyprinter.strategy

import engineer.echo.easyprinter.IMonitor

/**
 *  PrintStrategy.kt
 *  Info: 蓝牙打印机策略
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/15 - 3:34 PM
 *  more about me: http://www.1991th.com
 */
interface PrintStrategy {
    /**
     * 排除设备名为空的扫描结果
     */
    fun removeDeviceOfEmptyName(): Boolean

    /**
     * 寻找目标 如果为空 则一直寻找
     */
    fun targetName(): String?

    /**
     * 全局监听
     */
    fun getGlobalMonitor(): IMonitor?

    /**
     * 是否开启LiveData
     */
    fun enableLiveData(): Boolean
}