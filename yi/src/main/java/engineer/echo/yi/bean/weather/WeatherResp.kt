package engineer.echo.yi.bean.weather

import engineer.echo.easyapi.Result

data class WeatherResp(
    val error: Int = -1,
    val status: String = "error",
    val date: String = "1970-01-01",
    val results: List<WeatherResult> = arrayListOf()
) : Result() {

    fun getWeather(defVal: String = "nil"): String {
        return results.firstOrNull()?.weather_data?.firstOrNull()?.date ?: defVal
    }
}