package engineer.echo.easyapi.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.Companion.toException
import engineer.echo.easyapi.MD5Tool
import engineer.echo.easyapi.download.DownloadHelper.calculateProgress
import engineer.echo.easyapi.download.DownloadHelper.resumeBytes
import engineer.echo.easyapi.download.DownloadHelper.writeToFile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Type

class LiveDataDownloadAdapter : CallAdapter<ResponseBody, LiveData<DownloadState>> {

    private val liveData = MutableLiveData<DownloadState>()
    private val downloadState = DownloadState()

    override fun adapt(call: Call<ResponseBody>): LiveData<DownloadState> {
        EasyApi.printLog("LiveDataDownloadAdapter adapt")
        val url = call.request().url().url().toString()
        downloadState.url = url
        val path = call.request().tag(String::class.java)
        if (path is String) {
            downloadState.exception = null
            downloadState.path = path
            downloadState.id =
                MD5Tool.getMD5("[EasyApi][$url][$path]")
            downloadState.state = State.OnStart
            liveData.postValue(downloadState)

            call.enqueue(object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    EasyApi.printLog("adapt onFailure %s", t.message)
                    DownloadHelper.deleteIfExist(path)
                    downloadState.exception = t
                    liveData.postValue(downloadState)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        val resumeBytes = call.resumeBytes()
                        val total = body.contentLength() + resumeBytes
                        downloadState.exception = null
                        downloadState.total = total
                        body.byteStream()
                            .writeToFile(File(path), resumeBytes > 0) { state, current, msg ->
                                val cur = current + resumeBytes
                                downloadState.state = state
                                downloadState.current = cur
                                downloadState.progress = calculateProgress(cur, total)
                                downloadState.msg = msg ?: ""
                                EasyApi.printLog(
                                    "adapt onResponse writeToFile %s %s %s",
                                    state,
                                    downloadState.progress,
                                    msg
                                )
                                liveData.postValue(downloadState)
                            }
                    } else {
                        DownloadHelper.deleteIfExist(path)
                        downloadState.exception = response.toException().also {
                            EasyApi.printLog("adapt onResponse error %s", it.message)
                        }
                        liveData.postValue(downloadState)
                    }
                }
            })
        } else {
            downloadState.exception = Exception("-1", Throwable("invalid savedPath"))
            liveData.postValue(downloadState)
        }

        return liveData
    }

    override fun responseType(): Type = ResponseBody::class.java

}