package engineer.echo.yi.consumer.ui.bitmap

import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.LiveData

object BitmapContract {

    interface IView {
        fun toast(view: View)
        fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean)
    }

    interface IViewModel {
        val easyJobData: LiveData<Long>
        fun calculate(index: Int)
    }

    interface IModel {

        fun easyJob(index: Long): LiveData<Long>
    }
}