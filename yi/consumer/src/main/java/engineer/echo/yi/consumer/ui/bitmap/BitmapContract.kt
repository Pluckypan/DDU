package engineer.echo.yi.consumer.ui.bitmap

import android.view.View
import androidx.lifecycle.LiveData

object BitmapContract {

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