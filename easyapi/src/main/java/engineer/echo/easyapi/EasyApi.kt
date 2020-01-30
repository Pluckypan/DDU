package engineer.echo.easyapi

import android.app.Application
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EasyApi {

    companion object {

        private const val TAG = "EasyApi"
        private lateinit var application: Application
        private var customRetrofit: Retrofit? = null
        private var monitor: EasyMonitor? = null
        private var debugMode = false
        private const val DEFAULT_URL = "http://www.1991th.com/"

        internal fun printLog(format: String, vararg args: Any?) {
            if (debugMode) {
                Log.i(TAG, format.format(*args).plus(" thread=[${Thread.currentThread().name}]"))
            }
        }

        /**
         * 设置 Header
         */
        private fun addHeaderInterceptor(): Interceptor {
            return Interceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder().apply {
                    header("powerBy", TAG)
                    header("author", "plucky")
                }
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }

        /**
         * OkHttpClient
         */
        private val okClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(addHeaderInterceptor())
                .connectTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .build()
        }

        fun init(
            app: Application,
            retrofit: Retrofit? = null,
            debugMode: Boolean = false,
            monitor: EasyMonitor? = null
        ) {
            this.application = app
            this.customRetrofit = retrofit
            this.debugMode = debugMode
            this.monitor = monitor
        }

        fun <T> create(service: Class<T>): T {
            return lazyApi.create(service)
        }

        private val lazyApi by lazy {
            val builder = customRetrofit?.newBuilder() ?: Retrofit.Builder()
                .apply {
                    if (customRetrofit == null) {
                        client(okClient)
                        baseUrl(DEFAULT_URL)
                        addConverterFactory(GsonConverterFactory.create())
                    }
                }
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create(monitor))
            builder.build()
        }

    }
}