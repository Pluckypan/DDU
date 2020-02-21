package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData
import engineer.echo.yi.consumer.cmpts.weibo.User
import engineer.echo.yi.consumer.cmpts.weibo.bean.Account

object MainContract {

    interface IView {
        fun onAuthorize()
        fun onClearAuth()
        fun onRefreshAuth()
    }

    interface IViewModel {
        val userInfoData: LiveData<User>
        val accountInfoData: LiveData<Account>
        fun triggerToken()
        fun refreshAccountInfo(uid: String)
    }
}