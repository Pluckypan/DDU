package engineer.echo.study.ui.arch.coroutines

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IpLocation(
    val status: String = "",
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionName: String = "",
    val city: String = "",
    val zip: String = "",
    val lat: Float = 0f,
    val lon: Float = 0f,
    val timezone: String = "",
    val isp: String = "",
    val org: String = "",
    @SerializedName("as")
    val street: String = "",
    val query: String = ""
) : Parcelable {

    fun isLocateSuccess(): Boolean = status == "success"

    fun getQueryLocation(): String = if (isLocateSuccess()) "$lon,$lat" else "beijing"

    fun getLocation(defVal: String = "nil"): String = StringBuilder().apply {
        if (isLocateSuccess()) {
            appendln("$city $regionName $country")
            appendln("timezone: $timezone [${getQueryLocation()}]")
            appendln("ip: $query")
        } else {
            appendln(defVal)
        }
    }.toString()
}