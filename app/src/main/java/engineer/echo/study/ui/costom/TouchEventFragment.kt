package engineer.echo.study.ui.costom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment

/**
 *  TouchEventFragment.kt
 *  Info: TouchEventFragment.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/7/2 - 6:36 PM
 *  more about me: http://www.1991th.com
 */
class TouchEventFragment : BaseFragment() {

    companion object {

        fun goto(fragment: MasterFragment) {
            Request(TouchEventFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_touch_event, container, false)
    }

    override fun getTitle(): Int {
        return R.string.label_touch_app
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allowSwipeBack(false)
    }
}