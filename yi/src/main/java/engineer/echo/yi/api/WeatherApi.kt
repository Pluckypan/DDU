package engineer.echo.yi.api

import androidx.lifecycle.LiveData
import engineer.echo.yi.BuildConfig
import engineer.echo.yi.bean.weather.WeatherResp
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        private const val API_URL = "http://api.map.baidu.com/telematics/v3/"
    }

    @GET(API_URL.plus("{func}"))
    fun getWeather(
        @Path("func") func: String = "weather",
        @Query("output") output: String = "json",
        @Query("ak") ak: String = BuildConfig.BAIDU_AK,
        @Query("location") location: String
    ): LiveData<WeatherResp>
}