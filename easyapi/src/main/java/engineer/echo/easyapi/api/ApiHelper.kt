package engineer.echo.easyapi.api

import androidx.core.text.isDigitsOnly
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.pub.MD5Tool
import okhttp3.Request
import retrofit2.Response

internal object ApiHelper {

    fun cancel(id: String) {
        EasyApi.getClient()?.dispatcher()?.let {
            it.runningCalls().plus(it.queuedCalls()).firstOrNull { call ->
                !call.isCanceled && MD5Tool.getMD5(call.request().toString()) == id
            }?.cancel()
        }
    }

    fun Response<*>.contentSize(): Long {
        headers()["Content-Length"]?.let {
            if (it.isDigitsOnly()) return it.toLong()
        }
        return raw().networkResponse()?.contentSize() ?: 0
    }

    fun okhttp3.Response.contentSize(): Long {
        headers()["Content-Length"]?.let {
            if (it.isDigitsOnly()) return it.toLong()
        }
        return body()?.contentLength() ?: 0
    }

    fun Request.contentSize(): Long {
        headers()["Content-Length"]?.let {
            if (it.isDigitsOnly()) return it.toLong()
        }
        return body()?.contentLength() ?: 0
    }
}