package engineer.echo.yi.ui.main

import engineer.echo.easyapi.annotation.JobApi

@JobApi(uniqueId = "AAA")
interface ProxyApi {

    fun add(a: Int, b: Int): Int

    fun showToast(msg: String)

    fun print():String

    fun go()
}