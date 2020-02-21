package engineer.echo.yi

import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.common.Proxy

class App : EasyApp() {

    override fun onCreate() {
        super.onCreate()
        onAppInit(this)
    }

    companion object {

        fun onAppInit(app: EasyApp) {
            EasyApi.getProxy(Proxy.ConsumerApi::class.java).onAppInit(app)
            EasyApi.getProxy(Proxy.IMApi::class.java).onAppInit(app)
            EasyApi.getProxy(Proxy.LiveApi::class.java).onAppInit(app)
            EasyApi.getProxy(Proxy.ProducerApi::class.java).onAppInit(app)
        }
    }
}