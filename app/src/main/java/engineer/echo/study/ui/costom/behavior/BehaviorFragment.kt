package engineer.echo.study.ui.costom.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment


/**
 *  BehaviorFragment.kt
 *  Info: BehaviorFragment.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/26 - 10:07 PM
 *  more about me: http://www.1991th.com
 */
class BehaviorFragment : BaseFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(BehaviorFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_behavoir, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allowSwipeBack(false)
        view.findViewById<View>(R.id.v_green_behavior).setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    v.x = event.rawX - v.width / 2
                    v.y = event.rawY - v.height * 3 / 2
                }
            }
            return@setOnTouchListener false
        }
    }

    override fun getTitle(): Int {
        return R.string.label_behavior_app
    }
}