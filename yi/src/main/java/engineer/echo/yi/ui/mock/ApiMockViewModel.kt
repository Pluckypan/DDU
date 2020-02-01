package engineer.echo.yi.ui.mock

import androidx.lifecycle.*
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
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

    override fun startDownload(owner: LifecycleOwner) {
        // TODO 实现方式不优雅
        model.download().observe(owner, Observer {
            downloadData.postValue(it)
        })
    }

    override fun cancelDownload() {
        model.cancelDownload()
    }
}