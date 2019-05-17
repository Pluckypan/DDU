package engineer.echo.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.library.FlowAdapter
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.study.C.Companion.BEZIER
import engineer.echo.study.databinding.MainBinding

@Configuration(theme = R.style.Theme_AppCompat_Light)
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
        mBinding.flowBezierApp.setAdapter(object : FlowAdapter<String>(BEZIER) {
            override fun getView(p0: Int): View {
                return TextView(this@MainFragment.context).also {
                    it.text = BEZIER[p0]
                }
            }
        })
    }
}