package engineer.echo.yi

import android.app.Application
import android.util.DisplayMetrics
import androidx.annotation.StringRes
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result

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
    }

    companion object {

        private lateinit var app: Application

        fun getApp(): Application = app

        fun getString(@StringRes id: Int, vararg args: Any): String = getApp().getString(id, args)

        val displayMetrics: DisplayMetrics by lazy {
            app.resources.displayMetrics
        }
    }
}