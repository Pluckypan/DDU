package engineer.echo.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.study.databinding.MainBinding

class MainFragment : MasterFragment() {

    private lateinit var mBinding: MainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvQuadBezierApp.setOnClickListener {
            BezierViewFragment.goto(this)
        }
    }
}