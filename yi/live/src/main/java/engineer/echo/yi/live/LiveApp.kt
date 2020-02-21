package engineer.echo.yi.live

import engineer.echo.yi.common.EasyApp

class LiveApp: EasyApp() {
    override fun onCreate() {
        super.onCreate()
        onAppInit(this)
    }

    companion object {
        fun onAppInit(app: EasyApp) {

        }
    }
}