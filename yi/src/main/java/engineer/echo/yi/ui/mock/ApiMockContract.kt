package engineer.echo.yi.ui.mock

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp

object ApiMockContract {

    interface IView {
        fun onDownloadClick(view: View)
        fun onCancelDownloadClick(view: View)
    }

    interface IViewModel {
        val locationData: LiveData<IpLocation>
        val weatherData: LiveData<WeatherResp>
        val titleData: LiveData<String>
        val indicatorData: MutableLiveData<Pair<Int, Int>>
        val downloadData: MutableLiveData<DownloadState>
        fun startDownload(apk: Boolean = false)
        fun cancelDownload()
        fun refresh()
    }

    interface IModel {
        fun download(apk: Boolean = false): LiveData<DownloadState>
    }
}