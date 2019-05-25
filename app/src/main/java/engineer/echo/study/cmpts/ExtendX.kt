package engineer.echo.study.cmpts

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.florent37.viewanimator.ViewAnimator
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

fun View.bottomIn(
    duration: Long = 350,
    interceptor: Interpolator = DecelerateInterpolator()
) {
    ViewAnimator.animate(this)
        .translationY(this.height * 1f, 0f)
        .alpha(0f, 1.0f)
        .interpolator(interceptor)
        .duration(duration)
        .start()
}

fun View.bottomOut(
    duration: Long = 350,
    interceptor: Interpolator = AccelerateInterpolator()
) {
    ViewAnimator.animate(this)
        .translationY(0f, this.height * 1f)
        .alpha(1.0f, 0f)
        .interpolator(interceptor)
        .duration(duration)
        .start()
}