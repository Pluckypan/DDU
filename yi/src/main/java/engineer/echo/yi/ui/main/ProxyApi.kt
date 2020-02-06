package engineer.echo.yi.ui.main

import androidx.annotation.StringRes
import engineer.echo.easyapi.annotation.JobApi

@JobApi(uniqueId = "AAA")
interface ProxyApi {

    fun add(a: Int, b: Int): Int

    fun showToast(@StringRes id: Int)
}