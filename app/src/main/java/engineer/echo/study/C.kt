package engineer.echo.study

import android.graphics.Typeface
import engineer.echo.study.entity.Subject

class C {

    companion object {

        private const val ICON_PATH = "font/iconfont.ttf"

        var ICON = lazy {
            Typeface.createFromAsset(App.getApp().assets, ICON_PATH)
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