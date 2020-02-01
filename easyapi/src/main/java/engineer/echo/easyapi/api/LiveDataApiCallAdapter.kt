package engineer.echo.easyapi.api

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.Companion.toException
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result
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
    private val liveData = MutableLiveData<T>()

    override fun adapt(call: Call<T>): LiveData<T> {
        callTime.set(SystemClock.elapsedRealtime())
        EasyApi.printLog("LiveDataApiCallAdapter adapt")
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
        EasyApi.printLog("postResult adapt $method cost = %sms", cost)
        if (response != null && response.isSuccessful && t == null) {
            monitor?.onResult(true, response.body(), cost)
            liveData.postValue(response.body())
        } else {
            val constructor = rawType.declaredConstructors.first()
            // 通过无参构造函数实例化对象
            val result = constructor.newInstance() as T
            result.exception = t ?: response?.toException()
            monitor?.onResult(false, result, cost)
            liveData.postValue(result)
        }
    }

    override fun responseType(): Type = responseType
}