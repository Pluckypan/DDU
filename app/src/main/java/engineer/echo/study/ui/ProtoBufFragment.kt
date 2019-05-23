package engineer.echo.study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.proto.UserEntity
import engineer.echo.study.R
import engineer.echo.study.databinding.ProtoBufBinding

@Configuration(theme = R.style.Theme_AppCompat_Light)
class ProtoBufFragment : MasterFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(ProtoBufFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: ProtoBufBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_protobuf, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserEntity.User.newBuilder().apply {
            email = "plucky@echo.engineer"
            id = 1
            name = "Plucky"
            val phone1 = UserEntity.User.PhoneNumber.newBuilder()
                .setNumber("10086")
                .setType(UserEntity.User.PhoneType.HOME)
                .build()
            val phone2 = UserEntity.User.PhoneNumber.newBuilder()
                .setNumber("10010")
                .setType(UserEntity.User.PhoneType.WORK)
                .build()
            addPhone(phone1)
            addPhone(phone2)
        }.build().also {
            mBinding.user = it.toString()
        }
    }
}