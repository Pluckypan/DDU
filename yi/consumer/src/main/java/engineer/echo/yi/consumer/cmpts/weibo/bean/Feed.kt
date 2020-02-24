package engineer.echo.yi.consumer.cmpts.weibo.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import engineer.echo.easyapi.Result
import engineer.echo.yi.consumer.cmpts.weibo.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feed(
    @SerializedName("created_at")
    val createdAt: String,
    val id: String,
    val text: String,
    val source: String,
    val favorited: Boolean = false,
    val geo: Geo? = null,
    val mid: String,
    @SerializedName("reposts_count")
    val repostCount: String,
    @SerializedName("comments_count")
    val commentCount: String,
    val user: User? = null
) : Result(), Parcelable