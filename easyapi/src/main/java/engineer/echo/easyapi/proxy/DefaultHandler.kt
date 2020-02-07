package engineer.echo.easyapi.proxy

import java.lang.reflect.Proxy
import java.lang.reflect.Type

internal class DefaultHandler<T>(private val jobApiClz: Class<T>) : EasyHandler<T>(Any()) {

    override val proxy: T
        get() {
            return Proxy.newProxyInstance(jobApiClz.classLoader, arrayOf(jobApiClz), this) as T
        }

    companion object {

        fun getDefaultValue(returnType: Type): Any {
            return if (returnType == Void::class.java) {
                0
            } else {
                -1
            }
        }
    }
}