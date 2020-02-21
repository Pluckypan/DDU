package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData
import engineer.echo.yi.consumer.cmpts.weibo.User

object MainContract {

    interface IView {
        fun onAuthorize()
        fun onClearAuth()
        fun onRefreshAuth()
    }

    interface IViewModel {
        val userInfoData: LiveData<User>
        fun triggerToken()
    }
}