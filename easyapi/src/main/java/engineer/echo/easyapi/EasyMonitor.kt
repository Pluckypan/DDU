package engineer.echo.easyapi

interface EasyMonitor {
    fun onResult(
        isSuccess: Boolean,
        result: Result?,
        cost: Long,
        requestSize: Long,
        responseSize: Long
    )

    fun applyHeaderParams(url: String): HashMap<String, String>?

    fun applyCommonParams(url: String, method: String): HashMap<String, String>?

}