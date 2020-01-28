package engineer.echo.easyapi

import android.app.Application
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EasyApi {

    companion object {

        private lateinit var application: Application
        private var customRetrofit: Retrofit? = null

        /**
         * 设置 Header
         */
        private fun addHeaderInterceptor(): Interceptor {
            return Interceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder().apply {
                    header("AuthorId", "plucky")
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
            retrofit: Retrofit? = null
        ) {
            application = app
            customRetrofit = retrofit
        }

        fun <T> create(service: Class<T>): T {
            return lazyApi.create(service)
        }

        private val lazyApi by lazy {
            val builder = customRetrofit?.newBuilder() ?: Retrofit.Builder()
                .addCallAdapterFactory(LiveDataCallAdapterFactory()).apply {
                    // 如果是自定义的 Retrofit 则所有参数均由外部决定
                    if (customRetrofit == null) {
                        client(okClient)
                        addConverterFactory(GsonConverterFactory.create())
                    }
                }
            builder.build()
        }

    }
}