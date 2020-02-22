package engineer.echo.yi.im.ui.kotlin

import android.view.View
import androidx.lifecycle.LiveData

object KotlinContract {

    interface IView {
        fun toast(view: View)
    }

    interface IViewModel {
        val easyJobData: LiveData<Long>
        fun calculate(index: Int)
    }

    interface IModel {

        fun easyJob(index: Long): LiveData<Long>
    }
}