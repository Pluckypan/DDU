package engineer.echo.easyapi.job

import com.google.gson.Gson
import engineer.echo.easyapi.annotation.EasyJobHelper
import okhttp3.Request

internal object JobHelper {


    val Parser by lazy {
        Gson()
    }

    fun Request.isEasyJob(): Boolean {
        return EasyJobHelper.isEasyJobRequest(url().toString())
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