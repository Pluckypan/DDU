package engineer.echo.easyapi.api

import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.pub.MD5Tool

internal object ApiHelper {

    fun cancel(id: String) {
        EasyApi.getClient()?.dispatcher()?.let {
            it.runningCalls().plus(it.queuedCalls()).firstOrNull { call ->
                !call.isCanceled && MD5Tool.getMD5(call.request().toString()) == id
            }?.cancel()
        }
    }
}