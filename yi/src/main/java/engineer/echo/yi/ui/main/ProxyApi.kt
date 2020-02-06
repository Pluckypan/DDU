package engineer.echo.yi.ui.main

import androidx.annotation.StringRes
import engineer.echo.easylib.toastLong
import engineer.echo.yi.YiApp

interface ProxyApi {

    fun add(a: Int, b: Int): Int

    fun showToast(@StringRes id: Int)
}

class ProxyServer : ProxyApi {

    override fun add(a: Int, b: Int): Int = a + b

    override fun showToast(id: Int) = YiApp.getString(id).toastLong(YiApp.getApp())
}