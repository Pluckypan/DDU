package engineer.echo.yi.consumer.cmpts.weibo.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import engineer.echo.easyapi.Result
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserList(
    val users: List<Account> = arrayListOf(),
    @SerializedName("next_cursor")
    val nextCursor: Int = 0,
    @SerializedName("previous_cursor")
    val previousCursor: Int = 0,
    @SerializedName("total_number")
    val totalNumber: Int = 0
) : Result(), Parcelable {

}