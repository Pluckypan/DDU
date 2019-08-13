package engineer.echo.study

import android.graphics.Typeface
import android.os.SystemClock
import androidx.databinding.BindingAdapter
import com.googlecode.protobuf.format.JsonFormat
import engineer.echo.proto.UserEntity
import engineer.echo.study.entity.Subject
import net.cryptobrewery.syntaxview.SyntaxView

class C {

    companion object {

        private const val ICON_PATH = "font/iconfont.ttf"
        private const val SONGTI_PATH = "font/jiansong.ttf"
        private const val HEITI_PATH = "font/jianhei.ttf"
        const val URL_APK = "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"

        var ICON = lazy {
            Typeface.createFromAsset(App.getApp().assets, ICON_PATH)
        }
        var JIAN_SONG = lazy {
            Typeface.createFromAsset(App.getApp().assets, SONGTI_PATH)
        }
        var JIAN_HEI = lazy {
            Typeface.createFromAsset(App.getApp().assets, HEITI_PATH)
        }

        var USER = lazy {
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
            }.build()
        }

        fun newUser(name: String? = null): UserEntity.User {
            return UserEntity.User.newBuilder()
                .mergeFrom(USER.value)
                .setExtra("${SystemClock.uptimeMillis()}")
                .apply {
                    if (name != null && name.isNotEmpty()) {
                        setName(name)
                    }
                }
                .build()
        }

        fun ByteArray.toUser(): UserEntity.User? {
            return try {
                val builder = UserEntity.User.newBuilder()
                builder.mergeFrom(this)
                builder.build()
            } catch (e: Exception) {
                null
            }
        }

        @JvmStatic
        @BindingAdapter("syntaxCode")
        fun bindSyntaxCode(syntaxView: SyntaxView, syntaxCode: String?) {
            syntaxView.code.isEnabled = false
            syntaxView.code.setLines(50)
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

        var SUBJECTS = listOf(
            Subject(
                R.string.title_subject_bezier_app,
                R.color.color000066,
                R.string.iconBezier,
                listOf(
                    R.string.label_quad_bezier_app,
                    R.string.label_quad_bezier_manual_app
                )
            ),
            Subject(
                R.string.title_subject_protobuf_app,
                R.color.color006633,
                R.string.iconProtoBuf,
                listOf(
                    R.string.label_entry_level_protobuf_app,
                    R.string.label_advanced_protobuf_app,
                    R.string.label_high_level_protobuf_app
                )
            ),
            Subject(
                R.string.title_subject_ipc_app,
                R.color.color0099FF,
                R.string.iconIPC,
                listOf(
                    R.string.label_wifi_p2p_app,
                    R.string.label_messenger_app,
                    R.string.label_bluetooth_printer_app
                )
            ),
            Subject(
                R.string.title_subject_arch_app,
                R.color.color009966,
                R.string.iconArch,
                listOf(
                    R.string.label_room_arch,
                    R.string.label_work_manager_arch,
                    R.string.label_paging_arch,
                    R.string.label_coroutines_arch,
                    R.string.label_rxjava_arch
                )
            ),
            Subject(
                R.string.title_subject_custom_view,
                R.color.colorCC9933,
                R.string.iconCustomView,
                listOf(
                    R.string.label_behavior_app
                )
            )
        )
    }

}