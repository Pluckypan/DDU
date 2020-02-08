package engineer.echo.easyapi.api

import android.os.SystemClock
import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.toException
import engineer.echo.easyapi.EasyLiveData
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.api.ApiHelper.contentSize
import engineer.echo.easyapi.pub.MD5Tool
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicLong

internal class LiveDataApiCallAdapter<T : Result>(
    private val rawType: Class<*>,
    private val responseType: Type,
    private val monitor: EasyMonitor? = null
) :
    CallAdapter<T, LiveData<T>> {

    private var callTime = AtomicLong()
    private var requestSize = 0L
    private val liveData = EasyLiveData<T>()

    override fun adapt(call: Call<T>): LiveData<T> {
        callTime.set(SystemClock.elapsedRealtime())
        requestSize = call.request().contentSize()
        liveData.id = MD5Tool.getMD5(call.request().toString())
        EasyApi.printLog(
            "LiveDataApiCallAdapter adapt %s id=%s",
            call.request().method(), liveData.id
        )
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                postResult("onFailure", null, t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                postResult("onResponse", response, null)
            }
        })
        return liveData
    }

    @Suppress("UNCHECKED_CAST")
    private fun postResult(method: String, response: Response<T>? = null, t: Throwable? = null) {
        val cost = SystemClock.elapsedRealtime() - callTime.get()

        val responseSize = response?.contentSize() ?: 0
        EasyApi.printLog(
            "postResult adapt $method cost = %sms request[%s] response[%s]",
            cost, requestSize, responseSize
        )
        if (response != null && response.isSuccessful && t == null) {
            monitor?.onResult(true, response.body(), cost, requestSize, responseSize)
            liveData.postValue(response.body())
        } else {
            val constructor = rawType.declaredConstructors.first()
            // 通过无参构造函数实例化对象
            val result = constructor.newInstance() as T
            result.exception = t ?: response?.toException()
            monitor?.onResult(false, result, cost, requestSize, responseSize)
            liveData.postValue(result)
        }
    }

    override fun responseType(): Type = responseType
}