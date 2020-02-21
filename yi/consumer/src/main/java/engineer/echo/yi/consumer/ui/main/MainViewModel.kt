package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.consumer.cmpts.weibo.User
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.cmpts.weibo.api.Users
import engineer.echo.yi.consumer.cmpts.weibo.bean.Account

class MainViewModel : ViewModel(), MainContract.IViewModel {


    private val tokenTrigger = MutableLiveData<Boolean>()
    private val accountTrigger = MutableLiveData<String>()

    override fun triggerToken() {
        tokenTrigger.value = true
    }

    override fun refreshAccountInfo(uid: String) {
        accountTrigger.value = uid
    }

    override val userInfoData: LiveData<User> = Transformations.switchMap(tokenTrigger) {
        MutableLiveData<User>().apply {
            value = Weibo.getUser().also {
                refreshAccountInfo(it.id)
            }
        }
    }

    override val accountInfoData: LiveData<Account> = Transformations.switchMap(accountTrigger) {
        EasyApi.create(Users::class.java).getUser(it)
    }

    init {
        triggerToken()
    }

    override fun onCleared() {
        super.onCleared()
    }


}