package engineer.echo.yi

import android.app.Application
import android.util.DisplayMetrics
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyMonitor
import engineer.echo.easyapi.Result

class YiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        EasyApi.init(this, debugMode = BuildConfig.DEBUG, monitor = object : EasyMonitor {

            override fun onResult(isSuccess: Boolean, result: Result?, cost: Long) {

            }

        })
    }

    companion object {

        private lateinit var app: Application

        fun getApp(): Application = app

        val displayMetrics: DisplayMetrics by lazy {
            app.resources.displayMetrics
        }
    }
}