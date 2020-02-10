package engineer.echo.easyapi.download

import android.os.Parcelable
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.annotation.State
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadState(
    var id: String = "",
    var url: String = "",
    var path: String = "",
    var msg: String = ""
) : ProgressResult(), Parcelable {

    private fun noException(): Boolean = exception == null

    fun isDownloadSuccess(): Boolean {
        return super.isSuccess()
    }

    fun downloadEnable(): Boolean =
        (state != State.OnStart && state != State.OnProgress) || !noException()

    fun downloadText(): String =
        if (noException()) "$state:$progress $msg" else "${exception?.message}:${exception?.cause?.message}"
}