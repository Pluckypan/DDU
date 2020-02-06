package engineer.echo.easyapi.proxy

import engineer.echo.easyapi.EasyApi
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

internal class EasyHandler<T> internal constructor(private val client: Any) : InvocationHandler {

    val proxy: T
        get() {
            val clz = client.javaClass
            return Proxy.newProxyInstance(clz.classLoader, clz.interfaces, this) as T
        }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any {
        return try {
            if (args != null) {
                method.invoke(client, *args)
            } else {
                method.invoke(client)
            }
        } catch (e: Exception) {
            EasyApi.printLog(
                "EasyHandler invoke returnType = %s  error = %s",
                method.returnType.simpleName,
                e.cause?.message ?: e.message
            )
            if (method.returnType == Void::class.java) {
                0
            } else {
                -1
            }
        }
    }
}