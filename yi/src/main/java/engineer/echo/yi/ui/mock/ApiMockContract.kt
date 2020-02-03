package engineer.echo.yi.ui.mock

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp

object ApiMockContract {

    interface IView {
        fun onDownloadClick(view: View)
        fun onCancelDownloadClick(view: View)
        fun onZipClick(view: View)
    }

    interface IViewModel {
        val locationData: LiveData<IpLocation>
        val weatherData: LiveData<WeatherResp>
        val titleData: LiveData<String>
        val downloadData: LiveData<DownloadState>
        val zipData: LiveData<Result>
        val indicatorData: MutableLiveData<Pair<Int, Int>>
        val switchData: MutableLiveData<Boolean>
        fun startDownload()
        fun cancelDownload()
        fun startZipAction()
        fun refresh()
    }

    interface IModel {
        fun download(apk: Boolean = false): LiveData<DownloadState>
        fun zipAction(zip: Boolean): LiveData<Result>
    }
}