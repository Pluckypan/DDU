package engineer.echo.yi.common.cpmts.widgets.topbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.databinding.BindingAdapter
import engineer.echo.yi.common.R
import engineer.echo.yi.common.cpmts.icon.EasyIcon
import kotlinx.android.synthetic.main.common_layout_topbar.view.*

class TopBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.common_layout_topbar, this)
        context.obtainStyledAttributes(attrs, R.styleable.TopBarView).use {
            setTitle(it.getString(R.styleable.TopBarView_topBarTitle))
            setCloseIcon(
                it.getResourceId(
                    R.styleable.TopBarView_topBarCloseIcon,
                    R.string.common_icon_close
                )
            )
        }
    }

    fun setTitle(@StringRes title: Int) {
        setTitle(resources.getString(title))
    }

    fun setCloseIcon(@StringRes icon: Int) {
        EasyIcon.onBindEasyIcon(commonTopCloseTv, icon)
    }

    fun setTitle(title: String?) {
        commonTopTitleTv.text = title
    }

    fun setCloseClickListener(listener: OnClickListener) {
        commonTopCloseTv.isClickable = true
        commonTopCloseTv.setOnClickListener(listener)
    }

    companion object {

        @BindingAdapter("onTopbarClose")
        @JvmStatic
        fun onBindCloseListener(view: TopBarView, listener: OnClickListener) {
            view.setCloseClickListener(listener)
        }
    }
}