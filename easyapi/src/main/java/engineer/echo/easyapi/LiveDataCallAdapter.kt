package engineer.echo.easyapi

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  LiveDataCallAdapter.kt
 *  Info: LiveData 适配器 参考 https://github.com/square/retrofit
 *  Created by Plucky(plucky@echo.engineer) on 2020-01-27 - 18:44
 *  more about me: http://www.1991th.com
 */

class LiveDataCallAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<T>> {

    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {

            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {//确保执行一次
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val value = ApiResponse<T>(null, -1, t.message ?: "") as T
                            postValue(value)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type = responseType
}