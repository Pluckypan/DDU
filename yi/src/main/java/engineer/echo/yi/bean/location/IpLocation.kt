package engineer.echo.yi.bean.location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import engineer.echo.easyapi.Result
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
) : Result(), Parcelable {

    fun getQueryLocation(): String = "$lon,$lat"

    fun getLocation(defVal: String = "nil"): String = StringBuilder().apply {
        if (isSuccess() && status == "success") {
            appendln("$city $regionName $country")
            appendln("timezone: $timezone [${getQueryLocation()}]")
            appendln("ip: $query")
        } else {
            appendln(defVal)
        }
    }.toString()
}