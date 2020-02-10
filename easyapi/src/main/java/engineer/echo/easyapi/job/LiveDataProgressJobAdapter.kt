package engineer.echo.easyapi.job

import android.os.SystemClock
import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyApi.toException
import engineer.echo.easyapi.EasyLiveData
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.annotation.State
import engineer.echo.easyapi.api.ApiHelper.contentSize
import engineer.echo.easyapi.job.JobHelper.jobId
import engineer.echo.easyapi.job.JobHelper.jobMethod
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicLong

internal class LiveDataProgressJobAdapter<T : ProgressResult>(
    private val rawType: Class<*>,
    private val responseType: Type,
    private val monitor: EasyMonitor? = null
) :
    CallAdapter<T, LiveData<T>> {

    private var callTime = AtomicLong()
    private var requestSize = 0L
    private var jobMethod: String = ""
    // 实例化对象用于复用
    private val progressData by lazy {
        val constructor = rawType.declaredConstructors.first()
        // 通过无参构造函数实例化对象
        constructor.newInstance() as T
    }
    private val liveData by lazy {
        EasyLiveData<T>()
    }

    override fun adapt(call: Call<T>): LiveData<T> {
        val requestId = call.request().jobId()
        jobMethod = call.request().jobMethod() ?: ""
        if (JobHelper.progressJobTask.containsKey(requestId)) {
            EasyApi.printLog(
                "LiveDataProgressJobAdapter adapt job[%s] is running id=%s", jobMethod, requestId
            )
            return JobHelper.progressJobTask[requestId] as LiveData<T>
        }
        callTime.set(SystemClock.elapsedRealtime())
        requestSize = call.request().contentSize()
        liveData.id = requestId
        EasyApi.printLog(
            "LiveDataProgressJobAdapter adapt start job[%s] id=%s", jobMethod, requestId
        )
        // Start
        progressData.exception = null
        progressData.current = 0
        progressData.total = 0
        progressData.progress = 0
        progressData.state = State.OnStart
        postProgress(progressData, false)

        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                // Failed
                progressData.exception = t
                progressData.state = State.OnFail
                postProgress(progressData, true)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    // 原封不动转发执行结果「让JobServer更加灵活」
                    // Finish
                    postProgress(data, true)
                } else {
                    // Failed
                    progressData.exception = response.toException()
                    progressData.state = State.OnFail
                    postProgress(progressData, true)
                }
            }
        })

        return liveData
    }

    override fun responseType(): Type = responseType

    private fun postProgress(data: T, removeTask: Boolean) {
        val cost = SystemClock.elapsedRealtime() - callTime.get()
        val isSuccess = data.isSuccess()
        EasyApi.printLog(
            "postResult adapt $jobMethod cost = %sms request[%s] response[%s] isSuccess = %s error = %s",
            cost, requestSize, 0, isSuccess, data.exception?.message ?: "nil"
        )
        monitor?.onResult(isSuccess, data, cost, requestSize, 0)
        liveData.postValue(data)
        if (removeTask) {
            JobHelper.progressJobTask.remove(liveData.id)
        }
    }
}