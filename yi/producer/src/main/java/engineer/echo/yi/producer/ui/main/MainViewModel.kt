package engineer.echo.yi.producer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.easyapi.EasyApi
import engineer.echo.easylib.externalCache
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.producer.cmpts.zip.ZipApiRetrofit
import engineer.echo.yi.producer.cmpts.zip.ZipState
import java.io.File

class MainViewModel : ViewModel(), MainContract.IViewModel {

    private val unzipTrigger = MutableLiveData<Boolean>()

    override val unzipData: LiveData<ZipState> = Transformations.switchMap(unzipTrigger) {
        if (it == true)
            EasyApi.create(ZipApiRetrofit::class.java).unzipProgress(zipPath, unzipFolder)
        else
            null
    }


    override fun unzip() {
        unzipTrigger.value = true
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {

        val zipPath: String by lazy {
            File(EasyApp.getApp().externalCache(), "2019-07-18.zip").path
        }

        val unzipFolder: String = File(EasyApp.getApp().externalCache(), "job").path
    }
}