package engineer.echo.yi.bean.weather

import android.os.Parcelable
import engineer.echo.easyapi.Result
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherResp(
    val error: Int = -1,
    val status: String = "error",
    val date: String = "1970-01-01",
    val results: List<WeatherResult> = arrayListOf()
) : Result(),Parcelable {

    fun getWeather(defVal: String = "nil"): String {
        results.firstOrNull()?.let {
            return StringBuilder().apply {
                appendln("${it.currentCityCamelCase()} $date")

                it.weather_data.firstOrNull()?.let { today ->
                    appendln()
                    appendln(today.realTime())
                    appendln(today.getWeatherData())
                }
                it.index.forEach {
                    appendln(it.getIndex())
                }
                it.weather_data.getOrNull(1)?.let { tomorrow ->
                    appendln()
                    appendln("Tomorrow")
                    appendln(tomorrow.getWeatherData())
                }
            }.toString()
        }
        return defVal
    }

    fun simple(defVal: String = "nil"): String {
        results.firstOrNull()?.let {
            return StringBuilder().apply {
                it.weather_data.firstOrNull()?.let { today ->
                    appendln("${it.currentCityCamelCase()} ${today.realTime()}")
                }
            }.toString()
        }
        return defVal
    }
}