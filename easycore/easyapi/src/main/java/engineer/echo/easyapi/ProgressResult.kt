package engineer.echo.easyapi

import android.os.Parcelable
import androidx.annotation.Keep
import engineer.echo.easyapi.annotation.State
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
open class ProgressResult(
    var state: State = State.Idle,
    var total: Long = 0,
    var current: Long = 0,
    var progress: Int = 0
) : Result(), Parcelable {

    override fun isSuccess(): Boolean {
        return super.isSuccess() && state == State.OnFinish
    }
}