package engineer.echo.yi.consumer.cmpts.weibo.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import engineer.echo.easyapi.Result
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    val id: String = "",
    val name: String = "",
    // m:male f:female n:unknown
    val gender: String = "n",
    @SerializedName("screen_name")
    val screenName: String = "",
    @SerializedName("avatar_large")
    val avatar: String = "",
    @SerializedName("profile_url")
    val url: String = "",
    val description: String = "",
    val province: Int = 0,
    val city: Int = 0,
    val location: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    val lang: String = "en"
) : Result(), Parcelable {

}