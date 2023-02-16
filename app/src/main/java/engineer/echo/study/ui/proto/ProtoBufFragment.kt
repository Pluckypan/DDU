package engineer.echo.study.ui.proto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.ProtoBufBinding

class ProtoBufFragment : BaseFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(ProtoBufFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: ProtoBufBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_protobuf, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {

            tvToStringProtobuf.setOnClickListener {

            }
            tvToJsonProtobuf.setOnClickListener {

            }
            tvToEntityProtobuf.setOnClickListener {

            }
        }

    }

    override fun getTitle(): Int {
        return R.string.label_entry_level_protobuf_app
    }
}