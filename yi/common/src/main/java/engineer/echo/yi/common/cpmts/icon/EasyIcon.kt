package engineer.echo.yi.common.cpmts.icon

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import engineer.echo.yi.common.EasyApp
import java.io.File

object EasyIcon {

    private val iconType by lazy {
        Typeface.createFromAsset(EasyApp.getApp().assets, "icon${File.separator}iconfont.ttf")
    }


    @BindingAdapter("easyIcon")
    @JvmStatic
    fun onBindEasyIcon(textView: TextView, @StringRes iconText: Int?) {
        textView.typeface = iconType
        textView.text = if (iconText != null) EasyApp.getString(iconText) else ""
    }

}