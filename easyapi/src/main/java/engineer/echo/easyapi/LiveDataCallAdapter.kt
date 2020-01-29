package engineer.echo.easyapi

import android.os.SystemClock
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

internal class LiveDataCallAdapter<T : Result>(private val responseType: Type) :
    CallAdapter<T, LiveData<T>> {

    @Suppress("UNCHECKED_CAST")
    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {

            private val started = AtomicBoolean(false)
            private val callTime = SystemClock.elapsedRealtime()

            override fun onActive() {
                super.onActive()
                EasyApi.printLog("LiveDataCallAdapter onActive started = %s", started.get())
                if (started.compareAndSet(false, true)) {//确保执行一次
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            EasyApi.printLog(
                                "onActive onFailure t = %s cost = %sms",
                                t.message,
                                (SystemClock.elapsedRealtime() - callTime)
                            )
                            postValue(Result(t) as T)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            EasyApi.printLog(
                                "onActive onResponse cost = %sms",
                                (SystemClock.elapsedRealtime() - callTime)
                            )
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type = responseType
}