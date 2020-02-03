package engineer.echo.easyapi

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 *  ProgressResult.kt
 *  Info: 带处理进度的结果
 *  Created by Plucky(plucky@echo.engineer) on 2020-02-04 - 02:37
 *  more about me: http://www.1991th.com
 */
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