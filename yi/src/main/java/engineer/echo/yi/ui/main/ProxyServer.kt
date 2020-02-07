package engineer.echo.yi.ui.main

import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.easylib.toastLong
import engineer.echo.yi.common.EasyApp

@JobServer(uniqueId = "AAA")
class ProxyServer : ProxyApi {

    override fun add(a: Int, b: Int): Int = a + b

    override fun showToast(msg: String) = msg.toastLong(EasyApp.getApp())

    override fun print(): String {
        return "EasyApi ${add(1, 3)}"
    }

    override fun go() {
        showToast(print())
    }
}