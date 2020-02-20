package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData

object MainContract {

    interface IView {
        fun onAuthorize()
        fun onClearAuth()
        fun onRefreshAuth()
    }

    interface IViewModel {
        val tokenData: LiveData<String>
        fun triggerToken()
    }
}