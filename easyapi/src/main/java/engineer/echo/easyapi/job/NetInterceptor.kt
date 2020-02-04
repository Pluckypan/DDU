package engineer.echo.easyapi.job

import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.api.ApiHelper.contentSize
import okhttp3.Interceptor
import okhttp3.Response

class NetInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val before = System.nanoTime()
        val request = chain.request()
        val response = chain.proceed(request)
        EasyApi.printLog(
            "intercept cost = %.1fms,size = %s",
            (System.nanoTime() - before) / 1e6, response.contentSize()
        )
        return response
    }
}