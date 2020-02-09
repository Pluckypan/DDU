package engineer.echo.yi.bean.weather

import android.os.Parcelable
import engineer.echo.easylib.isInteger
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern

@Parcelize
data class WeatherIndex(
    val des: String,
    val tipt: String,
    val title: String,
    val zs: String
) : Parcelable {

    fun getIndex(): String {
        var index = "$tipt: $zs"
        (10 - index.length).let {
            return if (it > 0) {
                for (i in 0..it) {
                    index = index.plus("　")
                }
                index
            } else index
        }
    }
}

@Parcelize
data class WeatherData(
    val date: String,
    val dayPictureUrl: String,
    val nightPictureUrl: String,
    val weather: String,
    val wind: String,
    val temperature: String
) : Parcelable {

    fun realTime(): String {
        val start = date.indexOf("(")
        val end = date.indexOf(")")
        if (start == -1 || end == -1 || start >= end) return date
        return date.substring(start + 1, end)
            .replace("：", "")
    }

    fun getRealTimeTemperature(): Int {
        val real = realTime()
        val res = Pattern.compile("[^0-9]").matcher(real).replaceAll("")
        return if (res.isInteger()) res.toInt() else 0
    }

    fun getAverageTemperature(): Int {
        return getTemperatureArray().average().toInt()
    }

    fun getDateDesc(): String {
        return date.split(" ").first()
    }

    fun getTemperatureArray(): IntArray {
        val res = temperature.replace(" ~ ", ",").replace("℃", "").split(",")
        if (res.size != 2
            || !res[0].isInteger() || !res[1].isInteger()
        )
            return intArrayOf(0, 0)
        return intArrayOf(res[0].toInt(), res[1].toInt()).also {
            it.sort()
        }
    }

    fun getWeatherData(): String {
        return StringBuilder().apply {
            appendln("$weather $wind ${temperature.replace(" ~ ", "~")}")
        }.toString()
    }
}

@Parcelize
data class WeatherResult(
    val currentCity: String,
    val pm25: String,
    val index: List<WeatherIndex>,
    val weather_data: List<WeatherData>
) : Parcelable {

    fun currentCityCamelCase(): String {
        if (currentCity.isEmpty()) return currentCity
        return currentCity.replaceFirst(currentCity.first(), currentCity.first().toUpperCase())
    }

    fun getRealTimeTemperature(): Int {
        return weather_data.firstOrNull()?.getRealTimeTemperature() ?: 0
    }

    fun getTodayWeather(): String {
        return weather_data.firstOrNull()?.weather ?: ""
    }

    fun getTodayTemperature(): String {
        return weather_data.firstOrNull()?.temperature ?: ""
    }

    fun getTodayDate(): String {
        return weather_data.firstOrNull()?.getDateDesc() ?: ""
    }

    fun getTodayWind(): String {
        return weather_data.firstOrNull()?.wind ?: ""
    }

    fun getTodayWeatherIndex(): String {
        return StringBuilder().apply {
            index.forEachIndexed { index, it ->
                if (index % 2 == 0) {
                    append(it.getIndex()).append("      ")
                } else {
                    appendln(it.getIndex())
                }
            }
        }.toString()
    }

    fun getTodayEnvIndex(): String {
        if (!pm25.isInteger()) return pm25
        when (pm25.toInt()) {
            in 0..50 -> return "优"
            in 50..100 -> return "良"
            in 100..150 -> return "轻度污染"
            in 150..200 -> return "中度污染"
            in 200..300 -> return "中度污染"
            in 300..Int.MAX_VALUE -> return "严重污染"
        }
        return pm25
    }
}