package engineer.echo.yi.producer

import engineer.echo.yi.common.EasyApp

class ProducerApp:EasyApp() {
    override fun onCreate() {
        super.onCreate()
        onAppInit(this)
    }

    companion object {
        fun onAppInit(app: EasyApp) {

        }
    }
}