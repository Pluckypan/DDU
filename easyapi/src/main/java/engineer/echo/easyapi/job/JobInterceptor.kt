package engineer.echo.easyapi.job

import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.job.JobHelper.jobGroup
import engineer.echo.easyapi.job.JobHelper.jobId
import engineer.echo.easyapi.job.JobHelper.jobModule
import okhttp3.*

internal class JobInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val jobId = originRequest.jobId()
        val group = originRequest.jobGroup()
        val module = originRequest.jobModule()
        return if (jobId?.isNotEmpty() == true
            && group?.isNotEmpty() == true
            && module?.isNotEmpty() == true
        ) {
            originRequest.url().queryParameterNames().forEach {
                EasyApi.printLog("%s : %s",it,originRequest.url().queryParameter(it))
            }
            Response.Builder().body(
                ResponseBody.create(
                    MediaType.get("application/json; charset=UTF-8"),
                    JobHelper.Parser.toJson(DownloadState())
                )
            ).request(originRequest)
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("success")
                .build()
        } else {
            chain.proceed(originRequest)
        }

    }

}