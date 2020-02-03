package engineer.echo.easyapi.job

import com.google.gson.Gson
import okhttp3.Request
import retrofit2.Call

internal object JobHelper {

    private const val JOB_ID = "EasyJobId"
    private const val JOB_GROUP = "EasyJobGroup"
    private const val JOB_MODULE = "EasyJobModule"

    val Parser by lazy {
        Gson()
    }

    fun Request.jobId() = header(JOB_ID)
    fun Request.jobGroup() = header(JOB_GROUP)
    fun Request.jobModule() = header(JOB_MODULE)

    fun Call<*>.injectHeader(annotations: Array<Annotation>, jobId: String) {
        annotations.firstOrNull {
            it is EasyJob
        }?.let {
            if (it is EasyJob) {
                val header = request().headers()
                // 反射需要注意 proguard
                val field = header.javaClass.getDeclaredField("namesAndValues")
                field.isAccessible = true
                arrayListOf<String>().apply {
                    header.names().forEach { h ->
                        add("$h:${header[h]}")
                    }
                    add(JOB_ID)
                    add(jobId)
                    add(JOB_GROUP)
                    add(it.group)
                    add(JOB_MODULE)
                    add(it.module)
                }.also { arr ->
                    field.set(header, arr.toTypedArray())
                }
            }
        }
    }
}