package engineer.echo.study.ui.arch.coroutines

/**
 *  CoroutinesContract.ktt
 *  Info: CoroutinesContract.ktt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/25 - 8:47 PM
 *  more about me: http://www.1991th.com
 */
object CoroutinesContract {
    interface IView {
        fun onHelloClick()
        fun onHiClick()
        fun onWeatherClick()
    }
}