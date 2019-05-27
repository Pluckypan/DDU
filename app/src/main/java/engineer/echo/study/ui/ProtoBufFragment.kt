package engineer.echo.study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.googlecode.protobuf.format.JsonFormat
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.proto.UserEntity
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.databinding.ProtoBufBinding
import net.cryptobrewery.syntaxview.SyntaxView

@Configuration(theme = R.style.Theme_AppCompat_Light)
class ProtoBufFragment : MasterFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(ProtoBufFragment::class.java).also {
                fragment.startFragment(it)
            }
        }

        @JvmStatic
        @BindingAdapter("syntaxCode")
        fun bindSyntaxCode(syntaxView: SyntaxView, syntaxCode: String) {
            syntaxView.code.isEnabled = false
            syntaxView.code.setText(syntaxCode)
        }

        fun String.toUser(): UserEntity.User? {
            return try {
                val builder = UserEntity.User.newBuilder()
                JsonFormat.merge(this, builder)
                builder.build()
            } catch (e: Exception) {
                null
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
        mBinding.apply {
            code = JsonFormat.printToString(C.USER.value)
            tvToStringProtobuf.setOnClickListener {
                code = C.USER.value.toString()
            }
            tvToJsonProtobuf.setOnClickListener {
                code = JsonFormat.printToString(C.USER.value)
            }
            tvToEntityProtobuf.setOnClickListener {
                val user = syntaxProtobufApp.code.text.toString().toUser()
                code = user?.phoneList?.toString() ?: "Convert Failed."
            }
        }

    }
}