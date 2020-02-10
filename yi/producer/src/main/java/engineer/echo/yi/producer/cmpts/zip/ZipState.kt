package engineer.echo.yi.producer.cmpts.zip

import android.os.Parcelable
import engineer.echo.easyapi.ProgressResult
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZipState(var msg: String = "") : ProgressResult(), Parcelable {

    fun getZipDesc(): String {
        return "isSuccess=[${isSuccess()}] state=$state \n[$current,$total]progress=$progress \nmsg=[${exception?.message},$msg]"
    }
}