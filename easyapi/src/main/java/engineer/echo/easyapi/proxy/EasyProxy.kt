package engineer.echo.easyapi.proxy

import android.os.SystemClock
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.annotation.EasyJobHelper
import engineer.echo.easyapi.annotation.JobApi
import java.util.concurrent.ConcurrentHashMap

internal object EasyProxy {

    private val ownerMap by lazy {
        ConcurrentHashMap<Class<*>, Any>()
    }
    private val handlerMap by lazy {
        ConcurrentHashMap<Class<*>, EasyHandler<*>>()
    }

    fun <T> create(jobApiClz: Class<T>): T {
        val before = SystemClock.uptimeMillis()
        require(jobApiClz.isInterface) {
            "EasyApi EasyProxy: [${jobApiClz.simpleName}] is not an interface"
        }
        return if (ownerMap.containsKey(jobApiClz)) {
            ownerMap[jobApiClz] as T
        } else {
            getHandlerByAnno(jobApiClz).proxy.also {
                ownerMap[jobApiClz] = it as Any
                it
            }
        }.also {
            EasyApi.printLog("EasyProxy create cost = %sms", SystemClock.uptimeMillis() - before)
        }
    }

    private fun <T> getHandlerByAnno(jobApiClz: Class<T>): EasyHandler<T> {
        return if (handlerMap.containsKey(jobApiClz)) {
            handlerMap[jobApiClz] as EasyHandler<T>
        } else {
            val jobApi =
                jobApiClz.getAnnotation(JobApi::class.java)
            requireNotNull(jobApi) {
                "EasyApi EasyProxy: require add @JobApi to interface[${jobApiClz.simpleName}]"
            }
            val uniqueId = jobApi.uniqueId
            require(uniqueId.isNotEmpty()) {
                "EasyApi EasyProxy: uniqueId isEmpty of interface[${jobApiClz.simpleName}]"
            }
            val jobServer = EasyJobHelper.getObjectById(uniqueId)
            val handler = if (jobServer != null) {
                EasyHandler<T>(jobServer)
            } else {
                EasyApi.printLog("EasyProxy: cannot find JobServer by uniqueId[%s]", uniqueId)
                DefaultHandler(jobApiClz)
            }
            handlerMap[jobApiClz] = handler
            handler
        }
    }
}
