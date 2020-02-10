package engineer.echo.easyapi.job

import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.EasyJobHelper
import engineer.echo.easyapi.job.JobHelper.convertTo
import engineer.echo.easyapi.job.JobHelper.isEasyJob
import engineer.echo.easyapi.job.JobHelper.jobApi
import engineer.echo.easyapi.job.JobHelper.jobMethod
import okhttp3.*

internal class JobInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originRequest = chain.request()

        return if (originRequest.isEasyJob()) {
            val api = originRequest.jobApi()
            requireNotNull(api) {
                "EasyApi JobInterceptor: invalid api"
            }
            val method = originRequest.jobMethod()
            requireNotNull(method) {
                "EasyApi JobInterceptor: invalid method"
            }
            try {
                val obj = EasyApi.getProxy(Class.forName(api))
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
                // 由于存在 json 转换 所以返回父类的实例 子类再转换的时候不会有类型检查的错误
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