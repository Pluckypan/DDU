package engineer.echo.study

import android.widget.TextView
import androidx.databinding.BindingAdapter

object ExtendX {

    @JvmStatic
    @BindingAdapter("iconfont")
    fun bindIconfont(textView: TextView, iconfont: Boolean) {
        if (iconfont) {
            textView.typeface = C.ICON.value
        } else {
            textView.typeface = null
        }
    }
}