package engineer.echo.yi.api

import androidx.lifecycle.LiveData
import engineer.echo.yi.bean.weather.WeatherResp
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        private const val API_URL = "http://api.map.baidu.com/telematics/v3/"
        // 注意勿用于正式环境次数限制会被「封」
        private const val AK = "F1df490aae24d7a92cb9414d316b429e"
    }

    @GET(API_URL.plus("{func}"))
    fun getWeather(
        @Path("func") func: String = "weather",
        @Query("output") output: String = "json",
        @Query("ak") ak: String = AK,
        @Query("location") location: String
    ):LiveData<WeatherResp>
}