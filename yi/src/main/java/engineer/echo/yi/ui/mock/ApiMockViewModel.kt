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
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.weather.WeatherResp

class ApiMockViewModel : ViewModel(), ApiMockContract.IViewModel {

    private val model: ApiMockContract.IModel = ApiMockModel()

    override var weatherData: LiveData<WeatherResp> =
        EasyApi.create(WeatherApi::class.java).getWeather(location = "beijing")

    override var titleData: LiveData<String> = Transformations.map(weatherData) {
        it.simple(YiApp.getApp().getString(R.string.app_name))
    }

    override var downloadData: MutableLiveData<DownloadState> = MutableLiveData()

    override fun startDownload(apk: Boolean) {
        model.download(apk).assignTo(downloadData)
    }

    override fun cancelDownload() {
        downloadData.cancelRequest()
    }
}