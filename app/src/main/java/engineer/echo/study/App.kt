package engineer.echo.study

import android.app.Application
import android.util.DisplayMetrics
import com.jeremyliao.liveeventbus.LiveEventBus
import engineer.echo.easyprinter.EasyPrinter
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
        LiveEventBus.get().config().supportBroadcast(this)
        EasyPrinter.get().setup(this)
    }
}