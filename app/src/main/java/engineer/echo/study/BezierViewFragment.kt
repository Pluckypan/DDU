package engineer.echo.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.databinding.BezierBinding

class BezierViewFragment : MasterFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(BezierViewFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: BezierBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bezier, container, false)
        allowSwipeBack(false)
        return mBinding.root
    }
}