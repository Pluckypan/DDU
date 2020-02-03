package engineer.echo.yi.ui.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.pub.assignTo
import engineer.echo.easyapi.pub.cancelRequest
import engineer.echo.yi.R
import engineer.echo.yi.YiApp
import engineer.echo.yi.api.IpLocateApi
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp

class ApiMockViewModel : ViewModel(), ApiMockContract.IViewModel {

    private val model: ApiMockContract.IModel = ApiMockModel()

    override val locationData: LiveData<IpLocation> =
        EasyApi.create(IpLocateApi::class.java).getLocation()

    override val weatherData: LiveData<WeatherResp> =
        Transformations.switchMap(locationData) {
            EasyApi.create(WeatherApi::class.java).getWeather(location = it.getQueryLocation())
        }

    override val titleData: LiveData<String> = Transformations.map(locationData) {
        if (it.isSuccess() && it.city.isNotEmpty()) it.city else YiApp.getString(R.string.app_name)
    }

    override val indicatorData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    override val downloadData: MutableLiveData<DownloadState> = MutableLiveData()

    override fun startDownload(apk: Boolean) {
        model.download(apk).assignTo(downloadData)
    }

    override fun refresh() {
        // TODO 如果 locationData 重新 assignTo 会造成 接口循环调用
        Transformations.switchMap(locationData) {
            EasyApi.create(WeatherApi::class.java).getWeather(location = it.getQueryLocation())
        }.also {
            it.assignTo(weatherData as MutableLiveData<WeatherResp>)
        }
    }

    override fun cancelDownload() {
        downloadData.cancelRequest()
    }
}