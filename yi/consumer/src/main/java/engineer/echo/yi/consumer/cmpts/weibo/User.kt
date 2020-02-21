package engineer.echo.yi.consumer.cmpts.weibo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var token: String = "",
    var avatar: String = "",
    var expiresIn: Long = 0
) : Parcelable {

    override fun toString(): String {
        return "[id=$id token=$token]"
    }
}