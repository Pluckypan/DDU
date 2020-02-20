package engineer.echo.yi.consumer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.consumer.BuildConfig
import engineer.echo.yi.consumer.cmpts.weibo.Weibo

class MainViewModel : ViewModel(), MainContract.IViewModel {


    private val tokenTrigger = MutableLiveData<Boolean>()

    override fun triggerToken() {
        tokenTrigger.value = true
    }

    override val tokenData: LiveData<String> = Transformations.switchMap(tokenTrigger) {
        MutableLiveData<String>().also { it.value = Weibo.readToken() }
    }

    init {
        Weibo.install(EasyApp.getApp(), BuildConfig.DEBUG)
    }

    override fun onCleared() {
        super.onCleared()
    }


}