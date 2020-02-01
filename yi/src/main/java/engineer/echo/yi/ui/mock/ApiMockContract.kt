package engineer.echo.yi.ui.mock

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.bean.weather.WeatherResp

object ApiMockContract {

    interface IView {
        fun onDownloadClick(view: View)
        fun onCancelDownloadClick(view: View)
    }

    interface IViewModel {
        var weatherData: LiveData<WeatherResp>?
        var downloadData: MutableLiveData<DownloadState>
        fun startDownload(owner: LifecycleOwner)
        fun cancelDownload()
    }

    interface IModel {
        fun download(): LiveData<DownloadState>
        fun cancelDownload()
    }
}