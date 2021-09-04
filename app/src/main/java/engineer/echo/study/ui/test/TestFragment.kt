package engineer.echo.study.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.library.FlowAdapter
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.TestFragmentBinding
import engineer.echo.study.mvvm.vp2.Vp2Activity

class TestFragment : BaseFragment() {

    companion object {
        val TEST =
            listOf(
                R.string.label_recyclerview_app,
                R.string.label_touch_app,
                R.string.label_vp2
            )

        fun goto(fragment: BaseFragment) {
            Request(TestFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: TestFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allowSwipeBack(true)
        mBinding.apply {
            flowTestApp.setAdapter(object : FlowAdapter<Int>(TEST) {
                override fun getView(index: Int): View {
                    val itemView = View.inflate(context, R.layout.item_subject_flow_app, null)
                    itemView.findViewById<TextView>(R.id.tv_subject_item_app).apply {
                        setText(TEST[index])
                    }
                    return itemView
                }
            })
            flowTestApp.setOnItemClickListener { i, _ ->
                when (TEST[i]) {
                    R.string.label_recyclerview_app -> RecyclerViewFragment.goto(this@TestFragment)
                    R.string.label_touch_app -> TouchEventFragment.goto(this@TestFragment)
                    R.string.label_vp2 -> Vp2Activity.goto(requireActivity())
                }
            }
        }
    }

    override fun getTitle(): Int {
        return R.string.title_test_center_app
    }
}