package engineer.echo.yi

import android.app.Application
import engineer.echo.easyapi.EasyApi

class YiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        EasyApi.init(this, debugMode = BuildConfig.DEBUG)
    }
}