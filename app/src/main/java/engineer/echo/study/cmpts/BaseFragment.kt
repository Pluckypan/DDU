package engineer.echo.study.cmpts

import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.study.R

/**
 *  BaseFragment.kt
 *  Info: BaseFragment.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/26 - 7:49 PM
 *  more about me: http://www.1991th.com
 */
@Configuration(theme = R.style.Theme_AppCompat_Light)
abstract class BaseFragment : MasterFragment() {

    abstract fun getTitle(): Int
}