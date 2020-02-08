package engineer.echo.easyapi.job

import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.EasyJobHelper
import engineer.echo.easyapi.job.JobHelper.convertTo
import engineer.echo.easyapi.job.JobHelper.isEasyJob
import engineer.echo.easyapi.proxy.EasyProxy
import okhttp3.*

internal class JobInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originRequest = chain.request()

        return if (originRequest.isEasyJob()) {
            val api = originRequest.url().queryParameter(EasyJobHelper.EASY_JOB_API)
            requireNotNull(api) {
                "EasyApi JobInterceptor: invalid api"
            }
            val method = originRequest.url().queryParameter(EasyJobHelper.EASY_JOB_METHOD)
            requireNotNull(method) {
                "EasyApi JobInterceptor: invalid method"
            }
            try {
                val obj = EasyProxy.create(Class.forName(api))
                val argName = arrayListOf<Class<*>>()
                val argValue = arrayListOf<Any>()
                val headers = originRequest.headers()
                val httpUrl = originRequest.url()
                val size = headers.size()
                for (i in 0 until size) {
                    val key = headers.name(i)
                    val className = headers[key]
                    val clz = EasyJobHelper.transformToClass(className)
                    argName.add(clz)
                    argValue.add(httpUrl.queryParameter(key)!!.convertTo(clz))
                }
                val methodAction = obj.javaClass.getMethod(method, *argName.toTypedArray())
                methodAction.invoke(obj, *argValue.toTypedArray())
            } catch (e: Exception) {
                Result(e)
            }.let {
                Response.Builder().body(
                    ResponseBody.create(
                        MediaType.get("application/json; charset=UTF-8"),
                        JobHelper.Parser.toJson(it)
                    )
                ).request(originRequest)
                    .protocol(Protocol.HTTP_2)
                    .code(200)
                    .message("success")
                    .build()
            }
        } else {
            chain.proceed(originRequest)
        }

    }

}