package engineer.echo.study.ui.arch.coroutines

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Helper {

    val parser by lazy {
        Gson()
    }

    val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    val retrofit by lazy {
        Retrofit.Builder()
            .apply {
                client(client)
                baseUrl("http://www.1991th.com/")
                addConverterFactory(GsonConverterFactory.create())
            }
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }


    suspend fun getLocation(): IpLocation? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://ip-api.com/json/")
            .get()
            .build()
        client.newCall(request).execute().use {
            if (it.isSuccessful) {
                parser.fromJson(it.body()?.string(), IpLocation::class.java)
            } else {
                null
            }

        }
    }

    suspend fun getWeather(location: String): WeatherResp? = withContext(Dispatchers.IO) {
        val urlBuilder =
            HttpUrl.parse("http://api.map.baidu.com/telematics/v3/weather/")!!.newBuilder()
        val request = Request.Builder()
            .url(
                urlBuilder
                    .addQueryParameter("output", "json")
                    .addQueryParameter("ak", "O9lOtvH8lHG1caDoj0YDmi6P")
                    .addQueryParameter("location", location)
                    .build()
            )
            .build()
        client.newCall(request).execute().use {
            if (it.isSuccessful) {
                parser.fromJson(it.body()?.string(), WeatherResp::class.java)
            } else {
                null
            }

        }
    }
}