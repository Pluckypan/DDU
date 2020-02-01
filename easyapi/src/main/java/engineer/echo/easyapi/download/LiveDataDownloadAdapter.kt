package engineer.echo.easyapi.download

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.Companion.toException
import engineer.echo.easyapi.EasyMonitor
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
import java.util.concurrent.atomic.AtomicLong

class LiveDataDownloadAdapter(private val monitor: EasyMonitor? = null) :
    CallAdapter<ResponseBody, LiveData<DownloadState>> {

    private val liveData = MutableLiveData<DownloadState>()
    private val downloadState = DownloadState()
    private var callTime = AtomicLong()

    override fun adapt(call: Call<ResponseBody>): LiveData<DownloadState> {
        callTime.set(SystemClock.elapsedRealtime())
        val resumeBytes = call.resumeBytes()
        val urlAndPath = call.urlAndPath()
        val path = urlAndPath.second ?: ""
        downloadState.url = urlAndPath.first
        downloadState.path = path
        downloadState.id = downloadId(urlAndPath.first, path)
        downloadState.current = resumeBytes
        EasyApi.printLog(
            "LiveDataDownloadAdapter adapt id=%s resumeBytes=%s",
            downloadState.id, resumeBytes
        )
        // Start
        downloadState.exception = null
        downloadState.msg = ""
        downloadState.state = State.OnStart
        postDownload(downloadState)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                EasyApi.printLog("adapt onFailure %s", t.message)
                downloadState.exception = t
                downloadState.progress = 0
                downloadState.current = 0
                downloadState.state = State.OnFail
                postDownload(downloadState, true)
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
                            downloadState.progress = 0
                            downloadState.current = 0
                            downloadState.exception = DownloadHelper.invalidFileRange()
                            postDownload(downloadState, true)
                            return
                        }
                        realLen
                    } else {
                        bodyLen
                    }
                    downloadState.exception = null
                    downloadState.msg = ""
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
                            postDownload(
                                downloadState,
                                state == State.OnFail || state == State.OnFinish || state == State.OnCancel
                            )
                        }
                } else {
                    DownloadHelper.deleteIfExist(path)
                    downloadState.state = State.OnFail
                    downloadState.progress = 0
                    downloadState.current = 0
                    downloadState.exception = response.toException().also {
                        EasyApi.printLog(
                            "adapt onResponse error %s",
                            it.message?.plus(" - ${it.cause?.message}")
                        )
                    }
                    postDownload(downloadState, true)
                }
            }
        })

        return liveData
    }

    private fun postDownload(state: DownloadState, removeTask: Boolean = false) {
        if (state.state == State.OnFinish || state.state == State.OnFail || state.state == State.OnCancel) {
            monitor?.onResult(
                state.isDownloadSuccess(), state,
                SystemClock.elapsedRealtime() - callTime.get()
            )
        }
        liveData.postValue(state)
        if (removeTask) {
            DownloadHelper.downloadTask.remove(state.id)
        }
    }

    override fun responseType(): Type = ResponseBody::class.java

}