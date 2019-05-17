package engineer.echo.study

import android.graphics.Typeface

class C {

    companion object {

        private const val ICON_PATH = "font/iconfont.ttf"

        var ICON = lazy {
            Typeface.createFromAsset(App.getApp().assets, ICON_PATH)
        }

        var BEZIER = listOf("二阶", "二阶(定点)")
    }

}