package engineer.echo.study

import android.app.Application
import android.util.DisplayMetrics
import engineer.echo.study.cmpts.MMKVUtils

class App : Application() {

    companion object {
        private lateinit var mApp: App
        private lateinit var mDisplayMetrics: DisplayMetrics
        fun getApp(): Application = mApp
        fun getDisplayMetrics() = mDisplayMetrics
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
        mDisplayMetrics = resources.displayMetrics
        MMKVUtils.init(mApp)
    }
}