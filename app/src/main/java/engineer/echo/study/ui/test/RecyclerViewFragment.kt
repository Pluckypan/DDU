package engineer.echo.study.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import engineer.echo.easylib.formatLog
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.FragmentRecyclerviewBinding

class RecyclerViewFragment : BaseFragment() {

    companion object {
        private const val TAG = "RecyclerViewFragment"
        fun goto(fragment: MasterFragment) {
            Request(RecyclerViewFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: FragmentRecyclerviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allowSwipeBack(false)
        mBinding.rcvSize.apply {
            layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
            adapter = SizeRevAdapter()
        }
        mBinding.rangeseekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            mBinding.rcvSize.layoutParams.apply {
                width = ((maxValue.toFloat() / 100) * 20 * 150).toInt()
                "setOnRangeSeekbarChangeListener min=%s max=%s width=%s".formatLog(TAG, minValue, maxValue, width)
                mBinding.rcvSize.layoutParams=this
            }
        }
    }

    override fun getTitle(): Int {
        return R.string.label_recyclerview_app
    }
}