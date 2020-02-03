package engineer.echo.easyapi

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 *  Result.kt
 *  Info: EasyApi 处理结果,业务处理结果必须继承
 *  Created by Plucky(plucky@echo.engineer) on 2020-01-29 - 14:00
 *  more about me: http://www.1991th.com
 */
@Keep
@Parcelize
open class Result(
    var exception: Throwable? = null
) : Parcelable {

   open fun isSuccess(): Boolean = exception == null
}