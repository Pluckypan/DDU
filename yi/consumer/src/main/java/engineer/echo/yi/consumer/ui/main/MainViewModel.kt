package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.consumer.BuildConfig
import engineer.echo.yi.consumer.cmpts.weibo.User
import engineer.echo.yi.consumer.cmpts.weibo.Weibo

class MainViewModel : ViewModel(), MainContract.IViewModel {


    private val tokenTrigger = MutableLiveData<Boolean>()

    override fun triggerToken() {
        tokenTrigger.value = true
    }

    override val userInfoData: LiveData<User> = Transformations.switchMap(tokenTrigger) {
        MutableLiveData<User>().also { it.value = Weibo.getUser() }
    }

    init {
        Weibo.install(EasyApp.getApp(), BuildConfig.DEBUG)
        triggerToken()
    }

    override fun onCleared() {
        super.onCleared()
    }


}