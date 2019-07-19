package engineer.echo.study.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import engineer.echo.oneactivity.core.IMasterFragment
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.BezierPointBinding
import engineer.echo.study.ui.bezier.ManualQuadBezierView

/**
 *  BezierPointFragment.kt
 *  Info: 贝舍尔控制点输入界面
 *  Created by Plucky(plucky@echo.engineer) on 2019/5/20 - 3:55 PM
 *  more about me: http://www.1991th.com
 */
class BezierPointFragment : BaseFragment(), SeekBar.OnSeekBarChangeListener {

    companion object {
        private const val KEY_START_X = "KEY_START_X"
        private const val KEY_START_Y = "KEY_START_Y"
        private const val KEY_STOP_X = "KEY_STOP_X"
        private const val KEY_STOP_Y = "KEY_STOP_Y"
        private const val KEY_CTRL_X = "KEY_CTRL_X"
        private const val KEY_CTRL_Y = "KEY_CTRL_Y"
        fun goto(
            fragment: MasterFragment,
            startX: Float = 0f, startY: Float = 0f,
            stopX: Float = 60000f, stopY: Float = 1.0f,
            ctrlX: Float = 60000f, ctrlY: Float = 0f,
            reqCode: Int = 0
        ) {
            Request(BezierPointFragment::class.java).also {
                it.putExtra(KEY_START_X, startX.toInt())
                it.putExtra(KEY_START_Y, (startY * 100).toInt())
                it.putExtra(KEY_STOP_X, stopX.toInt())
                it.putExtra(KEY_STOP_Y, (stopY * 100).toInt())
                it.putExtra(KEY_CTRL_X, ctrlX.toInt())
                it.putExtra(KEY_CTRL_Y, (ctrlY * 100).toInt())
                fragment.startFragmentForResult(it, reqCode)
            }
        }

        fun parseData(request: Request, bezierView: ManualQuadBezierView) {
            val startX = request.getIntExtra(KEY_START_X, 0)
            val startY = request.getIntExtra(KEY_START_Y, 0) * 0.01f
            val stopX = request.getIntExtra(KEY_STOP_X, 0)
            val stopY = request.getIntExtra(KEY_STOP_Y, 0) * 0.01f
            val ctrlX = request.getIntExtra(KEY_CTRL_X, 0)
            val ctrlY = request.getIntExtra(KEY_CTRL_Y, 0) * 0.01f
            bezierView.start(startX * 1f, startY, stopX * 1f, stopY, ctrlX * 1f, ctrlY)
        }

        @JvmStatic
        @BindingAdapter("startX", "startY", "stopX", "stopY", "ctrlX", "ctrlY")
        fun onBindInfo(textView: TextView, startX: Int, startY: Int, stopX: Int, stopY: Int, ctrlX: Int, ctrlY: Int) {
            textView.text = String.format(
                "开始点(%d,%.1f) - 结束点(%d,%.1f) - 控制点(%d,%.1f)",
                startX,
                startY * 0.01f,
                stopX,
                stopY * 0.01f,
                ctrlX,
                ctrlY * 0.01f
            )
        }
    }

    private lateinit var mBinding: BezierPointBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bezier_point, container, false)
        mBinding.apply {
            startX = request.getIntExtra(KEY_START_X, 0)
            startY = request.getIntExtra(KEY_START_Y, 0)
            stopX = request.getIntExtra(KEY_STOP_X, 0)
            stopY = request.getIntExtra(KEY_STOP_Y, 0)
            ctrlX = request.getIntExtra(KEY_CTRL_X, 0)
            ctrlY = request.getIntExtra(KEY_CTRL_Y, 0)
        }.also { b ->
            b.tvConfirmPointBezier.setOnClickListener {
                Request().apply {
                    putExtra(KEY_START_X, b.startX)
                    putExtra(KEY_START_Y, b.startY)
                    putExtra(KEY_STOP_X, b.stopX)
                    putExtra(KEY_STOP_Y, b.stopY)
                    putExtra(KEY_CTRL_X, b.ctrlX)
                    putExtra(KEY_CTRL_Y, b.ctrlY)
                }.also {
                    setResult(IMasterFragment.RESULT_OK, it)
                    finish()
                }
            }
        }
        mBinding.apply {
            seekBarStartXBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
            seekBarStartYBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
            seekBarStopXBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
            seekBarStopYBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
            seekBarCtrlXBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
            seekBarCtrlYBezier.setOnSeekBarChangeListener(this@BezierPointFragment)
        }
        allowSwipeBack(false)
        return mBinding.root
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.seekBar_startX_bezier -> mBinding.startX = progress
            R.id.seekBar_startY_bezier -> mBinding.startY = progress
            R.id.seekBar_stopX_bezier -> mBinding.stopX = progress
            R.id.seekBar_stopY_bezier -> mBinding.stopY = progress
            R.id.seekBar_ctrlX_bezier -> mBinding.ctrlX = progress
            R.id.seekBar_ctrlY_bezier -> mBinding.ctrlY = progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun getTitle(): Int {
        return R.string.label_quad_bezier_manual_app
    }
}