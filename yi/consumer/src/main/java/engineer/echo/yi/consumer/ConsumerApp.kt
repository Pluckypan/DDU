package engineer.echo.yi.consumer

import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.consumer.cmpts.weibo.Weibo

class ConsumerApp : EasyApp() {

    override fun onCreate() {
        super.onCreate()
        onAppInit(this)
    }

    companion object {
        fun onAppInit(app: EasyApp) {
            Weibo.install(app, BuildConfig.DEBUG)
        }
    }
}