package engineer.echo.study.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.MainBinding
import engineer.echo.study.ui.BezierViewFragment
import engineer.echo.study.ui.ProtoBufFragment
import engineer.echo.study.ui.behavior.BehaviorFragment
import engineer.echo.study.ui.coroutines.CoroutinesFragment
import engineer.echo.study.ui.costom.RecyclerViewFragment
import engineer.echo.study.ui.costom.TouchEventFragment
import engineer.echo.study.ui.ipc.WifiP2pFragment
import engineer.echo.study.ui.messenger.MessengerFragment
import engineer.echo.study.ui.printer.PrinterFragment

class MainFragment : BaseFragment(), MainAdapter.MainAdapterCallback {

    private lateinit var mBinding: MainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.rcvSubjectMainApp.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(this@MainFragment)
        }
    }

    override fun onItemClick(pos: Int, subItemId: Int) {
        when (subItemId) {
            R.string.label_quad_bezier_app -> BezierViewFragment.goto(this)
            R.string.label_quad_bezier_manual_app -> BezierViewFragment.goto(this, true)
            R.string.label_entry_level_protobuf_app -> ProtoBufFragment.goto(this)
            R.string.label_wifi_p2p_app -> WifiP2pFragment.goto(this)
            R.string.label_messenger_app -> MessengerFragment.goto(this)
            R.string.label_bluetooth_printer_app -> PrinterFragment.goto(this)
            R.string.label_coroutines_arch -> CoroutinesFragment.goto(this)
            R.string.label_recyclerview_app -> RecyclerViewFragment.goto(this)
            R.string.label_behavior_app -> BehaviorFragment.goto(this)
            R.string.label_touch_app -> TouchEventFragment.goto(this)
        }
    }

    override fun getTitle(): Int {
        return R.string.app_name
    }
}