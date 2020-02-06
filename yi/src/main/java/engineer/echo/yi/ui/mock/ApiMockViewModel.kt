package engineer.echo.yi.ui.mock

import androidx.lifecycle.*
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.proxy.EasyProxy
import engineer.echo.easyapi.pub.cancelRequest
import engineer.echo.easylib.formatLog
import engineer.echo.yi.R
import engineer.echo.yi.YiApp
import engineer.echo.yi.api.IpLocateApi
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp
import engineer.echo.yi.ui.main.ProxyApi

class ApiMockViewModel(info: Int) : ViewModel(), ApiMockContract.IViewModel {

    init {
        "init %s".formatLog("ApiMockViewModel", info)
    }

    private val model: ApiMockContract.IModel = ApiMockModel()
    override val indicatorData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    override val switchData: MutableLiveData<Boolean> = MutableLiveData()

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

    override fun startDownload() {
        downloadTrigger.value = switchData.value == true
    }

    override fun cancelDownload() {
        downloadData.cancelRequest()
        EasyProxy.create(ProxyApi::class.java).also {
            it.showToast("${it.add(3,9)}")
            it.print()
            it.go()
        }
    }

    // 解压相关
    private val zipTrigger = MutableLiveData<Boolean>()

    override val zipData: LiveData<Result> = Transformations.switchMap(zipTrigger) {
        model.zipAction(it == true)
    }

    override fun startZipAction() {
        zipTrigger.value = switchData.value == true
    }

    class Factory(private val info: Int) :
        ViewModelProvider.AndroidViewModelFactory(YiApp.getApp()) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ApiMockViewModel(info) as T
        }
    }
}