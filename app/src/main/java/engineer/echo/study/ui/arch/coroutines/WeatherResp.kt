package engineer.echo.study.ui.arch.coroutines

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherResp(
    val error: Int = -1,
    val status: String = "error",
    val date: String = "1970-01-01",
    val results: List<WeatherResult> = arrayListOf()
) : Parcelable {

    fun isWeatherSuccess(): Boolean = results.isNotEmpty()

    fun getWeather(defVal: String = "nil"): String {
        results.firstOrNull()?.let {
            return StringBuilder().apply {
                appendln("${it.currentCityCamelCase()} $date")

                it.weather_data.firstOrNull()?.let { today ->
                    appendln()
                    appendln(today.realTime())
                    appendln(today.getWeatherData())
                }
                appendln(it.getTodayWeatherIndex())
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