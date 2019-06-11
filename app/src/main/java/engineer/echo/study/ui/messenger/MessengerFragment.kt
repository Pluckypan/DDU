package engineer.echo.study.ui.messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.study.R

/**
 *  MessengerFragment.kt
 *  Info: Messenger跨进程通信
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/11 - 9:38 AM
 *  more about me: http://www.1991th.com
 */
@Configuration(theme = R.style.Theme_AppCompat_Light)
class MessengerFragment : MasterFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}