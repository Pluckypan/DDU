package engineer.echo.yi.ui.coroutines

import android.view.View

object CoroutinesContract {

    interface IView {
        fun runJob(view: View)
    }

    interface IViewModel {

    }

    interface IModel {

    }
}