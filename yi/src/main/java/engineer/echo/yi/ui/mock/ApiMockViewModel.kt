package engineer.echo.yi.ui.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.pub.cancelRequest
import engineer.echo.yi.R
import engineer.echo.yi.YiApp
import engineer.echo.yi.api.IpLocateApi
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp

class ApiMockViewModel : ViewModel(), ApiMockContract.IViewModel {

    private val model: ApiMockContract.IModel = ApiMockModel()
    override val indicatorData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    // 刷新事件
    private val refreshTrigger = MutableLiveData<Boolean>()

    override val locationData: LiveData<IpLocation> = Transformations.switchMap(refreshTrigger) {
        if (it == true) {
            EasyApi.create(IpLocateApi::class.java).getLocation()
        } else {
            null
        }
    }

    override val weatherData: LiveData<WeatherResp> =
        Transformations.switchMap(locationData) {
            if (it != null) {
                EasyApi.create(WeatherApi::class.java).getWeather(location = it.getQueryLocation())
            } else {
                null
            }
        }

    override val titleData: LiveData<String> = Transformations.map(locationData) {
        if (it.isSuccess() && it.city.isNotEmpty()) it.city else YiApp.getString(R.string.app_name)
    }

    override fun refresh() {
        refreshTrigger.value = true
    }

    // 下载相关
    private val downloadTrigger = MutableLiveData<Boolean>()

    override val downloadData: LiveData<DownloadState> =
        Transformations.switchMap(downloadTrigger) { apk ->
            model.download(apk)
        }

    override fun startDownload(apk: Boolean) {
        downloadTrigger.value = apk
    }

    override fun cancelDownload() {
        downloadData.cancelRequest()
    }
}