package engineer.echo.yi.ui.main

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.bean.weather.WeatherResp

object MainContract {

    interface IView {
        fun onSubmitClick(view: View)
    }

    interface IViewModel {
        var weatherData: LiveData<WeatherResp>?
        var downloadData: MutableLiveData<DownloadState>
        fun startDownload(owner: LifecycleOwner)
    }

    interface IModel {
        fun download(): LiveData<DownloadState>
    }
}