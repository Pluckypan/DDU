package engineer.echo.easyapi.job

import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyLiveData
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.EasyJobHelper
import engineer.echo.easyapi.annotation.JobCallback
import engineer.echo.easyapi.annotation.State
import engineer.echo.easyapi.job.JobHelper.convertTo
import engineer.echo.easyapi.job.JobHelper.getFullException
import engineer.echo.easyapi.job.JobHelper.isEasyJob
import engineer.echo.easyapi.job.JobHelper.jobApi
import engineer.echo.easyapi.job.JobHelper.jobId
import engineer.echo.easyapi.job.JobHelper.jobMethod
import okhttp3.*
import java.lang.reflect.InvocationTargetException

internal class JobInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originRequest = chain.request()

        return if (originRequest.isEasyJob()) {
            val api = originRequest.jobApi()
            requireNotNull(api) {
                "EasyApi JobInterceptor: invalid api".also {
                    EasyApi.printError("JobInterceptor intercept error = %s", it)
                }
            }
            val method = originRequest.jobMethod()
            requireNotNull(method) {
                "EasyApi JobInterceptor: invalid method".also {
                    EasyApi.printError("JobInterceptor intercept error = %s", it)
                }
            }
            try {
                val obj = EasyApi.getProxy(Class.forName(api))
                val argName = arrayListOf<Class<*>>()
                val argValue = arrayListOf<Any>()
                val headers = originRequest.headers()
                val httpUrl = originRequest.url()
                val size = headers.size()
                val jobId = originRequest.jobId()
                val jobLiveData = EasyApi.obtainProgressJob<ProgressResult>(jobId)
                for (i in 0 until size) {
                    val key = headers.name(i)
                    val className = headers[key]
                    val clz = EasyJobHelper.transformToClass(className)
                    argName.add(clz)
                    if (clz == JobCallback::class.java && i == size - 1) {
                        argValue.add(object : JobCallback {
                            override fun onJobState(
                                state: State?,
                                total: Long,
                                current: Long,
                                progress: Int
                            ) {
                                jobLiveData?.let { liveData ->
                                    liveData.value?.let {
                                        it.progress = progress
                                        it.total = total
                                        it.current = current
                                        it.state = state ?: State.OnProgress
                                        it
                                    }?.also {
                                        if (liveData is EasyLiveData) {
                                            liveData.postValue(it)
                                        }
                                    }
                                }
                            }
                        })
                    } else {
                        argValue.add(httpUrl.queryParameter(key)!!.convertTo(clz))
                    }
                }
                val methodAction = obj.javaClass.getMethod(method, *argName.toTypedArray())
                val result = methodAction.invoke(obj, *argValue.toTypedArray())
                result?.apply {
                    if (this is ProgressResult) {
                        total = jobLiveData?.value?.total ?: 0
                        current = jobLiveData?.value?.current ?: 0
                        progress = jobLiveData?.value?.progress ?: 0
                    }
                }
                result
            } catch (e: Exception) {
                // 由于存在 json 转换 所以返回父类的实例 子类再转换的时候不会有类型检查的错误
                when (e) {
                    is InvocationTargetException -> Throwable(e.getFullException())
                    else -> e
                }.let {
                    Result(it)
                }
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