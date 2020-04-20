package engineer.echo.easyapi

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
open class Result(
    var exception: Throwable? = null
) : Parcelable {

    open fun isSuccess(): Boolean = exception == null
}