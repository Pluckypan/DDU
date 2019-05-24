package engineer.echo.study

import android.graphics.Typeface
import engineer.echo.proto.UserEntity
import engineer.echo.study.entity.Subject

class C {

    companion object {

        private const val ICON_PATH = "font/iconfont.ttf"
        private const val SONGTI_PATH = "font/jiansong.ttf"
        private const val HEITI_PATH = "font/jianhei.ttf"

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
            )
        )
    }

}