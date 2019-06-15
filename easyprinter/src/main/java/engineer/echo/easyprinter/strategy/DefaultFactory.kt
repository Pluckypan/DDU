package engineer.echo.easyprinter.strategy

/**
 *  DefaultFactory.kt
 *  Info: 默认工厂
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/15 - 3:38 PM
 *  more about me: http://www.1991th.com
 */
class DefaultFactory : StrategyFactory() {

    override fun create(): PrintStrategy {
        return DefaultStrategy()
    }
}