package engineer.echo.easyapi.download

import android.os.Parcelable
import engineer.echo.easyapi.Result
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadState(
    var id: String = "",
    var url: String = "",
    var path: String = "",
    var state: State = State.Idle,
    var progress: Int = 0,
    var msg: String = ""
) : Result(), Parcelable {

    fun downloadEnable(): Boolean =
        (state != State.OnStart && state != State.OnProgress) || !isSuccess()

    fun downloadText(): String =
        if (isSuccess()) "$state:$progress $msg" else "${exception?.message}:${exception?.cause?.message}"
}