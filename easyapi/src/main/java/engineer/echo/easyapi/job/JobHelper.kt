package engineer.echo.easyapi.job

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.annotation.EasyJobHelper
import okhttp3.Request
import java.util.concurrent.ConcurrentHashMap

internal object JobHelper {

    val progressJobTask by lazy {
        ConcurrentHashMap<String, LiveData<ProgressResult>>()
    }


    val Parser by lazy {
        Gson()
    }

    fun Request.isEasyJob(): Boolean {
        return EasyJobHelper.isEasyJobRequest(url().toString())
    }

    fun Request.jobApi(): String? {
        return url().queryParameter(EasyJobHelper.EASY_JOB_API)
    }

    fun Request.jobMethod(): String? {
        return url().queryParameter(EasyJobHelper.EASY_JOB_METHOD)
    }

    fun <T : ProgressResult> obtainProgressJob(id: String): LiveData<T>? {
        if (progressJobTask.containsKey(id)) return progressJobTask[id] as LiveData<T>
        return null
    }

    fun String.convertTo(clazz: Class<*>): Any {
        return when (clazz) {
            Byte::class.java -> this.toByte()
            Short::class.java -> this.toShort()
            Integer::class.java -> this.toInt()
            Long::class.java -> this.toLong()
            Float::class.java -> this.toFloat()
            Double::class.java -> this.toDouble()
            Boolean::class.java -> this.toBoolean()
            else -> Parser.fromJson(Parser.toJson(this), clazz)
        }
    }
}