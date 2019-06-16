package engineer.echo.easyprinter.strategy

import engineer.echo.easyprinter.IMonitor

/**
 *  DefaultStrategy.kt
 *  Info: 默认策略
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/15 - 3:35 PM
 *  more about me: http://www.1991th.com
 */
class DefaultStrategy : PrintStrategy {

    override fun removeDeviceOfEmptyName(): Boolean {
        return true
    }

    override fun targetName(): String? {
        return null
    }

    override fun getGlobalMonitor(): IMonitor? {
        return null
    }

    override fun enableLiveData(): Boolean {
        return true
    }
}