package engineer.echo.easyapi

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import engineer.echo.easyapi.api.ApiHelper
import engineer.echo.easyapi.download.DownloadHelper
import engineer.echo.easyapi.download.DownloadHelper.downloadInner
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.job.JobHelper
import engineer.echo.easyapi.job.JobInterceptor
import engineer.echo.easyapi.job.NetInterceptor
import engineer.echo.easyapi.proxy.EasyProxy
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

object EasyApi {

    private const val TAG = "EasyApi"
    private lateinit var application: Application
    private var customRetrofit: Retrofit? = null
    private var monitor: EasyMonitor? = null
    private var debugMode = false

    private const val DEFAULT_URL = "http://www.1991th.com/"

    internal fun printLog(format: String, vararg args: Any?) {
        if (debugMode) {
            Log.i(
                TAG,
                format.format(*args).plus(" thread=[${Thread.currentThread().id}:${Thread.currentThread().name}]")
            )
        }
    }

    internal fun Response<*>.toException(): Exception {
        return Exception("${code()}", Throwable(message().plus(" try again.")))
    }

    /**
     * 设置 Header
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder().apply {
                header("powerBy", TAG)
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * OkHttpClient
     */
    private val okClient by lazy {
        customRetrofit?.okClient().let {
            if (it != null) it.newBuilder() else OkHttpClient.Builder()
        }
            .addInterceptor(JobInterceptor())
            .addNetworkInterceptor(NetInterceptor())
            .apply {
                if (customRetrofit == null) {
                    addInterceptor(addHeaderInterceptor())
                    connectTimeout(15L, TimeUnit.SECONDS)
                    readTimeout(30L, TimeUnit.SECONDS)
                    writeTimeout(30L, TimeUnit.SECONDS)
                }
            }
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

    /**
     * 调用 Api 接口
     */
    fun <T> create(service: Class<T>): T {
        return lazyApi.create(service)
    }

    /**
     * 动态代理
     */
    fun <T> getProxy(jobApiClz: Class<T>): T {
        return EasyProxy.create(jobApiClz)
    }

    fun download(
        @Url url: String, path: String, resume: Boolean = true
    ): LiveData<DownloadState> {
        return downloadInner(url, path, resume)
    }

    fun obtainDownloadJob(downloadId: String): LiveData<DownloadState>? {
        return DownloadHelper.obtainDownloadJob(downloadId)
    }

    fun <T : ProgressResult> obtainProgressJob(id: String): LiveData<T>? {
        return JobHelper.obtainProgressJob(id)
    }

    fun cancelDownload(id: String) {
        DownloadHelper.cancelDownload(id)
    }

    fun downloadTaskExist(@Url url: String, path: String): Boolean {
        return DownloadHelper.downloadTaskExist(url, path)
    }

    fun Retrofit.okClient(): OkHttpClient? {
        return callFactory().let {
            if (it is OkHttpClient) return it else null
        }
    }

    fun getClient(): OkHttpClient? = lazyApi.okClient()

    /**
     * 取消正在队列中或执行中的 api ,不包括「下载」和「后台作业」
     */
    fun cancel(id: String) {
        ApiHelper.cancel(id)
    }

    /**
     * 取消所有网络请求，包括 api 和「下载」，不包括「后台作业」
     */
    fun cancelAll() {
        getClient()?.dispatcher()?.cancelAll()
    }

    private val lazyApi by lazy {
        val builder = customRetrofit?.newBuilder() ?: Retrofit.Builder()
            .apply {
                client(okClient)
                if (customRetrofit == null) {
                    baseUrl(DEFAULT_URL)
                    addConverterFactory(GsonConverterFactory.create())
                }
            }
            .addCallAdapterFactory(LiveDataCallAdapterFactory.create(monitor))
        builder.build()
    }
}