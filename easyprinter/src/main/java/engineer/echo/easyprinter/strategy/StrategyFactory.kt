package engineer.echo.easyprinter.strategy

/**
 *  StrategyFactory.kt
 *  Info: 策略工厂类
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/15 - 3:37 PM
 *  more about me: http://www.1991th.com
 */
abstract class StrategyFactory {
    abstract fun create(): PrintStrategy
}