package engineer.echo.yi.bean.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherIndex(
    val des: String,
    val tipt: String,
    val title: String,
    val zs: String
) : Parcelable

@Parcelize
data class WeatherData(
    val date: String,
    val dayPictureUrl: String,
    val nightPictureUrl: String,
    val weather: String,
    val wind: String,
    val temperature: String
) : Parcelable

data class WeatherResult(
    val currentCity: String,
    val pm25: String,
    val index: List<WeatherIndex>,
    val weather_data: List<WeatherData>
)