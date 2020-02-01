package engineer.echo.easyapi.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.Companion.toException
import engineer.echo.easyapi.download.DownloadHelper.calculateProgress
import engineer.echo.easyapi.download.DownloadHelper.downloadId
import engineer.echo.easyapi.download.DownloadHelper.resumeBytes
import engineer.echo.easyapi.download.DownloadHelper.urlAndPath
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
        val resumeBytes = call.resumeBytes()
        EasyApi.printLog("LiveDataDownloadAdapter adapt resumeBytes=%s", resumeBytes)
        val urlAndPath = call.urlAndPath()
        downloadState.url = urlAndPath.first
        downloadState.current = resumeBytes
        val path = urlAndPath.second
        if (path is String) {
            downloadState.path = path
            downloadState.id = downloadId(urlAndPath.first, path)
            val existCall = EasyApi.downloadTaskExist(urlAndPath.first, path)
            if (existCall) {
                downloadState.state = State.Idle
                downloadState.exception = DownloadHelper.downloadTaskExist()
                liveData.postValue(downloadState)
                return liveData
            }

            // Start
            downloadState.exception = null
            downloadState.msg = ""
            downloadState.state = State.OnStart
            liveData.postValue(downloadState)

            call.enqueue(object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    EasyApi.printLog("adapt onFailure %s", t.message)
                    downloadState.exception = t
                    downloadState.state = State.OnFail
                    liveData.postValue(downloadState)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        val bodyLen = body.contentLength()
                        val total = if (resumeBytes > 0) {
                            val realLen = DownloadHelper.getFileLength(urlAndPath.first)
                            if (bodyLen + resumeBytes != realLen) {
                                downloadState.exception = DownloadHelper.invalidFileRange()
                                liveData.postValue(downloadState)
                                return
                            }
                            realLen
                        } else {
                            bodyLen
                        }
                        downloadState.exception = null
                        downloadState.total = total
                        body.byteStream()
                            .writeToFile(File(path), resumeBytes) { state, current, msg ->
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
                        downloadState.state = State.OnFail
                        downloadState.exception = response.toException().also {
                            EasyApi.printLog(
                                "adapt onResponse error %s",
                                it.message?.plus(" - ${it.cause?.message}")
                            )
                        }
                        liveData.postValue(downloadState)
                    }
                }
            })
        } else {
            downloadState.state = State.Idle
            downloadState.exception = DownloadHelper.invalidSavedPath()
            liveData.postValue(downloadState)
        }

        return liveData
    }

    override fun responseType(): Type = ResponseBody::class.java

}