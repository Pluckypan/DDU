package engineer.echo.easyapi

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory private constructor(private val monitor: EasyMonitor? = null) :
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
        require(rawType.superclass == Result::class.java) { "EasyApi: rawType must be a subclass of Result" }
        val paramSize = rawType.constructors.first().parameterTypes.size
        // 必须定义无参构造函数
        require(paramSize == 0) { "EasyApi: class has no zero argument constructor" }
        EasyApi.printLog(
            "LiveDataCallAdapterFactory get rawType = %s,annotations = %s",
            rawType.simpleName,
            annotations.size
        )
        return LiveDataCallAdapter<Result>(rawType, resultType, monitor)
    }

    companion object {
        fun create(monitor: EasyMonitor? = null): LiveDataCallAdapterFactory =
            LiveDataCallAdapterFactory(monitor)
    }
}