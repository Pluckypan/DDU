package engineer.echo.easyapi

interface EasyMonitor {
    fun onResult(isSuccess: Boolean, result: Result?, cost: Long)
}