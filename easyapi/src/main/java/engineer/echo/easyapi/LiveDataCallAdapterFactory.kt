package engineer.echo.easyapi

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.api.LiveDataApiCallAdapter
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easyapi.download.LiveDataDownloadAdapter
import engineer.echo.easyapi.job.LiveDataProgressJobAdapter
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class LiveDataCallAdapterFactory private constructor(private val monitor: EasyMonitor? = null) :
    CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 返回类型必须是 LiveData
        if (getRawType(returnType) != LiveData::class.java) return null
        // 取 LiveData 泛型
        val resultType = getParameterUpperBound(0, returnType as ParameterizedType)
        // 必须指定了返回类型「参数化构造：必须指定类型」
        // require(resultType is ParameterizedType) { "EasyApi: resultType must be Parameterized" }
        val rawType = getRawType(resultType)
        // 必须是继承自 Result
        require(Result::class.java.isAssignableFrom(rawType)) {
            "EasyApi: rawType must be a subclass of Result".also {
                EasyApi.printError("LiveDataCallAdapterFactory error = %s", it)
            }
        }
        val paramSize = rawType.constructors.first().parameterTypes.size
        // 必须定义无参构造函数
        require(paramSize == 0) {
            "EasyApi: class require zero argument constructor".also {
                EasyApi.printError("LiveDataCallAdapterFactory error = %s", it)
            }
        }
        EasyApi.printLog(
            "LiveDataCallAdapterFactory get rawType = %s,annotations = %s",
            rawType.simpleName,
            annotations.size
        )
        return when {
            rawType == DownloadState::class.java -> LiveDataDownloadAdapter(monitor)
            ProgressResult::class.java.isAssignableFrom(rawType) -> LiveDataProgressJobAdapter<ProgressResult>(
                rawType,
                resultType,
                monitor
            )
            else -> LiveDataApiCallAdapter<Result>(rawType, resultType, monitor)
        }
    }

    companion object {
        fun create(monitor: EasyMonitor? = null): LiveDataCallAdapterFactory =
            LiveDataCallAdapterFactory(monitor)

        fun createObjectByType(type: Type): Any? {
            val rawType = getRawType(type)
            val paramSize = rawType.constructors.first().parameterTypes.size
            return if (paramSize == 0) rawType.constructors.first().newInstance() else null
        }
    }
}