package engineer.echo.easyapi.proxy

import engineer.echo.easyapi.annotation.EasyJobHelper
import engineer.echo.easyapi.annotation.JobApi
import java.util.concurrent.ConcurrentHashMap

object EasyProxy {

    private val ownerMap = ConcurrentHashMap<Class<*>, Any>()
    private val handlerMap = ConcurrentHashMap<Class<*>, EasyHandler<*>>()

    fun <T> create(jobApiClz: Class<T>): T {
        require(jobApiClz.isInterface) {
            "EasyApi EasyProxy: [${jobApiClz.simpleName}] is not an interface"
        }
        if (ownerMap.containsKey(jobApiClz)) {
            return ownerMap[jobApiClz] as T
        } else {
            getHandlerByAnno(jobApiClz).proxy.also {
                ownerMap[jobApiClz] = it as Any
                return it
            }
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
            requireNotNull(jobServer) {
                "EasyApi EasyProxy: cannot find JobServer by uniqueId[$uniqueId]"
            }
            val handler = EasyHandler<T>(jobServer)
            handlerMap[jobApiClz] = handler
            handler
        }
    }
}
