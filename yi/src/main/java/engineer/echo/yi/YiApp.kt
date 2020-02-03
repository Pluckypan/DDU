package engineer.echo.yi

import android.app.Application
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result
import engineer.echo.yi.cmpts.widgets.refresh.SmartRefresh

class YiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        EasyApi.init(this, debugMode = BuildConfig.DEBUG, monitor = object : EasyMonitor {

            override fun onResult(
                isSuccess: Boolean,
                result: Result?,
                cost: Long,
                requestSize: Long,
                responseSize: Long
            ) {

            }
        })
        SmartRefresh.setup()
    }

    companion object {

        private lateinit var app: Application

        fun getApp(): Application = app

        fun getString(@StringRes id: Int, vararg args: Any): String = getApp().getString(id, args)
        fun getColor(@ColorRes id: Int): Int = getApp().resources.getColor(id)

        val displayMetrics: DisplayMetrics by lazy {
            app.resources.displayMetrics
        }
    }
}