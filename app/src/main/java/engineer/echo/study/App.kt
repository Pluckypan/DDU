package engineer.echo.study

import android.app.Application

class App : Application() {

    companion object {
        private lateinit var mApp: App
        fun getApp(): Application = mApp
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
    }
}