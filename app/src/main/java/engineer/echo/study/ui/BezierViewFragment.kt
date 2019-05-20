package engineer.echo.study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R

class BezierViewFragment : MasterFragment() {

    companion object {
        private const val KEY_MANUAL = "key_for_manual"
        fun goto(fragment: MasterFragment, manual: Boolean = false) {
            Request(BezierViewFragment::class.java).also {
                it.putExtra(KEY_MANUAL, manual)
                fragment.startFragment(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val manual = request.getBooleanExtra(KEY_MANUAL, false)
        val layoutId = if (manual) R.layout.fragment_manual_bezier else R.layout.fragment_bezier
        allowSwipeBack(false)
        return layoutInflater.inflate(layoutId, container, false)
    }
}