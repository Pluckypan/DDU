package engineer.echo.yi.common

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result
import engineer.echo.easylib.HookManager
import engineer.echo.easylib.NotifierHelper
import engineer.echo.yi.common.cpmts.widgets.refresh.SmartRefresh
import me.weishu.reflection.Reflection

open class EasyApp : Application(), EasyMonitor {
    override fun onCreate() {
        super.onCreate()
        app = this
        EasyApi.init(this, debugMode = BuildConfig.DEBUG, monitor = this)
        SmartRefresh.setup()
        NotifierHelper.init(this)
        HookManager.hookActivityThread()
        // HookManager.hookAms(this)
        HookManager.hookSystemService()
        HookManager.hookNotificationManager(this)
        HookManager.hookToast(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Reflection.unseal(base)
    }

    override fun getSystemService(name: String): Any {
        Log.i("Plucky", "getSystemService $name")
        return super.getSystemService(name)
    }

    override fun onResult(
        isSuccess: Boolean,
        result: Result?,
        cost: Long,
        requestSize: Long,
        responseSize: Long,
    ) {

    }

    override fun applyHeaderParams(url: String): HashMap<String, String>? {
        return null
    }

    override fun applyCommonParams(url: String, method: String): HashMap<String, String>? {
        val consumerApi = EasyApi.getProxy(Proxy.ConsumerApi::class.java)
        return consumerApi.getRequestParams(url, method)
    }

    companion object {

        private lateinit var app: Application

        @JvmStatic
        fun getApp(): Application = app

        fun getString(@StringRes id: Int, vararg args: Any): String = getApp().getString(id, *args)
        fun getColor(@ColorRes id: Int): Int = getApp().resources.getColor(id)

        val displayMetrics: DisplayMetrics by lazy {
            app.resources.displayMetrics
        }
    }
}