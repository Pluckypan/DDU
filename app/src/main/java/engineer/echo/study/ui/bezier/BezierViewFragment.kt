package engineer.echo.study.ui.bezier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.IMasterFragment
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.ui.main.BezierPointFragment

class BezierViewFragment : BaseFragment() {

    companion object {
        private const val KEY_MANUAL = "key_for_manual"
        private const val REQ_CODE = 0x001
        fun goto(fragment: MasterFragment, manual: Boolean = false) {
            Request(BezierViewFragment::class.java).also {
                it.putExtra(KEY_MANUAL, manual)
                fragment.startFragment(it)
            }
        }
    }

    private var mManualBezierView: ManualQuadBezierView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val manual = request.getBooleanExtra(KEY_MANUAL, false)
        val layoutId = if (manual) R.layout.fragment_manual_bezier else R.layout.fragment_bezier
        allowSwipeBack(false)
        return layoutInflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mManualBezierView = view.findViewById<ManualQuadBezierView?>(R.id.manual_bezier_app)
        mManualBezierView?.apply {
            setOnClickListener {
                BezierPointFragment.goto(
                    this@BezierViewFragment,
                    startX,
                    startY,
                    stopX,
                    stopY,
                    ctrlX,
                    ctrlY,
                    REQ_CODE
                )
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Request?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (data != null && resultCode == IMasterFragment.RESULT_OK) {
            mManualBezierView?.let {
                BezierPointFragment.parseData(data, it)
            }
        }
    }

    override fun getTitle(): Int {
        return if (mManualBezierView != null) R.string.label_quad_bezier_manual_app else R.string.label_quad_bezier_app
    }
}