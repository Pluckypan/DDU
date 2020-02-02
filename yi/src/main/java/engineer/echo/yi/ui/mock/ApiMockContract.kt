package engineer.echo.yi.ui.mock

import android.view.View
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
        var weatherData: LiveData<WeatherResp>
        var titleData: LiveData<String>
        var downloadData: MutableLiveData<DownloadState>
        fun startDownload(apk: Boolean = false)
        fun cancelDownload()
    }

    interface IModel {
        fun download(apk: Boolean = false): LiveData<DownloadState>
    }
}