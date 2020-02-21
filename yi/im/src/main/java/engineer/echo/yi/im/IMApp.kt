package engineer.echo.yi.im

import engineer.echo.yi.common.EasyApp

class IMApp: EasyApp() {
    override fun onCreate() {
        super.onCreate()
        onAppInit(this)
    }

    companion object {
        fun onAppInit(app: EasyApp) {

        }
    }
}
