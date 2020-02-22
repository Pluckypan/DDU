package $currentPackage$

import android.view.View
import androidx.lifecycle.LiveData

object $moduleName$Contract {

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