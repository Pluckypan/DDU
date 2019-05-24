package engineer.echo.study.cmpts

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import engineer.echo.study.App
import engineer.echo.study.C

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

    @JvmStatic
    @BindingAdapter("jiansong")
    fun bindJianSong(textView: TextView, jiansong: Boolean) {
        if (jiansong) {
            textView.typeface = C.JIAN_SONG.value
        } else {
            textView.typeface = null
        }
    }

    @JvmStatic
    @BindingAdapter("jianhei")
    fun bindJianHei(textView: TextView, jianhei: Boolean) {
        if (jianhei) {
            textView.typeface = C.JIAN_HEI.value
        } else {
            textView.typeface = null
        }
    }
}

fun View.getColor(colorId: Int): Int = context.resources.getColor(colorId)
fun Float.toPx(): Int = (App.getDisplayMetrics().density * this + 0.5f).toInt()