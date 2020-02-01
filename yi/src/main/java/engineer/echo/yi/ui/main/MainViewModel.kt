package engineer.echo.yi.ui.main

import androidx.lifecycle.*
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.weather.WeatherResp

class MainViewModel : ViewModel(), MainContract.IViewModel {

    private val model: MainContract.IModel = MainModel()

    override var weatherData: LiveData<WeatherResp>? =
        EasyApi.create(WeatherApi::class.java).getWeather(location = "beijing")

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