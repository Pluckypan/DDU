package engineer.echo.yi.bean.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherIndex(
    val des: String,
    val tipt: String,
    val title: String,
    val zs: String
) : Parcelable {

    fun getIndex(): String {
        return "$tipt: $zs"
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
        return date.substring(date.indexOf("(") + 1, date.indexOf(")"))
            .replace("：", "")
    }

    fun getWeatherData(): String {
        return StringBuilder().apply {
            appendln("天气: $weather $wind")
            appendln("气温: ${temperature.replace(" ~ ", "~")}")
        }.toString()
    }
}

data class WeatherResult(
    val currentCity: String,
    val pm25: String,
    val index: List<WeatherIndex>,
    val weather_data: List<WeatherData>
) {
    fun currentCityCamelCase(): String {
        if (currentCity.isEmpty()) return currentCity
        return currentCity.replaceFirst(currentCity.first(), currentCity.first().toUpperCase())
    }
}