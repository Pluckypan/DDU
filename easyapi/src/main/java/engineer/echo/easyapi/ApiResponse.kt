package engineer.echo.easyapi

import androidx.annotation.Keep

@Keep
data class ApiResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)