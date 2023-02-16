package engineer.echo.easylib

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.IBinder
import android.util.Log
import org.joor.Reflect
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Proxy


@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
object HookManager {


    /**
     * ActivityThread
     */
    fun hookActivityThread() {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val sCurrentActivityThreadField: Field =
            activityThreadClass.getDeclaredField("sCurrentActivityThread")
        sCurrentActivityThreadField.isAccessible = true
        // 获取 activityThread 实例
        val activityThread: Any = sCurrentActivityThreadField.get(null) ?: return
        // 获取 activityThread handler
        val handlerField: Field = activityThreadClass.getDeclaredField("mH")
        handlerField.isAccessible = true
        val handler: Handler = handlerField.get(activityThread) as Handler
        val callBackField: Field = Handler::class.java.getDeclaredField("mCallback")
        callBackField.isAccessible = true
        // 替换 handler
        callBackField.set(handler, ActivityThreadHandlerCallback(handler))
    }


    /**
     * hook ActivityManager
     */
    fun hookAms(application: Application) {
        val activityManagerClass = Class.forName("android.app.ActivityManager")

        val iActivityManagerSingletonField =
            activityManagerClass.getDeclaredField("IActivityManagerSingleton")
        iActivityManagerSingletonField.isAccessible = true

        val iActivityManagerSingleton = iActivityManagerSingletonField[null]
        val singletonClass = Class.forName("android.util.Singleton")
        val instanceField = singletonClass.getDeclaredField("mInstance")
        instanceField.isAccessible = true

        val iActivityManagerClass = Class.forName("android.app.IActivityManager")
        val getServiceMethod: Method = activityManagerClass.getDeclaredMethod("getService")
        val iActivityManagerObj: Any = getServiceMethod.invoke(null) ?: return

        val proxyIActivityManager: Any = Proxy.newProxyInstance(
            application.classLoader,
            arrayOf(iActivityManagerClass)
        ) { _, method, args ->
            Log.i("Plucky", "method=$method args=$args")
            method.invoke(iActivityManagerObj, *args)
        }

        instanceField[iActivityManagerSingleton] = proxyIActivityManager
    }

    fun hookSystemService() {
        try {
            val x =
                Reflect.onClass("android.os.ServiceManager").field("sCache")
                    .get<Map<String, IBinder>>()
            for (i in x) {
                Log.i("Plucky", "SystemService i=${i.key}")
            }
        } catch (e: Throwable) {
            Log.e("Plucky", "hookPwm", e)
        }
    }


    fun hookNotificationManager(application: Application) {
        val originService =
            Reflect.onClass("android.app.NotificationManager").call("getService").get<Any>()
        val proxy = Proxy.newProxyInstance(
            application.classLoader,
            arrayOf(Class.forName("android.app.INotificationManager"))
        ) { _, method, args ->
            Log.d("Plucky", "invoke: method=$method")
            if (args != null && args.isNotEmpty()) {
                for (arg in args) {
                    Log.d("Plucky", "invoke: arg=$arg")
                }
            }
            method.invoke(originService, *args)
        }

        Reflect.onClass("android.app.NotificationManager").set("sService", proxy)
    }

    fun hookToast(application: Application) {
        try {
            val originService =
                Reflect.onClass("android.widget.Toast").call("getService").get<Any>()
            val proxy = Proxy.newProxyInstance(
                application.classLoader,
                arrayOf(Class.forName("android.app.INotificationManager"))
            ) { _, method, args ->
                Log.d("Plucky", "invoke: method=$method")
                if (args != null && args.isNotEmpty()) {
                    for (arg in args) {
                        Log.d("Plucky", "invoke: arg=$arg")
                    }
                }
                method.invoke(originService, *args)
            }

            Reflect.onClass("android.widget.Toast").set("sService", proxy)
        } catch (e: Throwable) {
            Log.e("Plucky", "hookToast", e)
        }
    }


}