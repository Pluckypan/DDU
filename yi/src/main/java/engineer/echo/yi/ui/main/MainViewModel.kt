package engineer.echo.yi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.api.WeatherApi
import engineer.echo.yi.bean.weather.WeatherResp

class MainViewModel : ViewModel(), MainContract.IViewModel {

    override var weatherData: LiveData<WeatherResp>? =
        EasyApi.create(WeatherApi::class.java).getWeather(location = "beijing")
}