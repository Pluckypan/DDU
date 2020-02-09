package engineer.echo.easyapi.proxy

import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import java.lang.reflect.Proxy
import java.lang.reflect.Type

internal class DefaultHandler<T>(private val jobApiClz: Class<T>) : EasyHandler<T>(Any()) {

    override val proxy: T
        get() {
            return Proxy.newProxyInstance(jobApiClz.classLoader, arrayOf(jobApiClz), this) as T
        }

    companion object {

        private fun errorDownload(e: Exception): DownloadState =
            DownloadState().also { it.exception = e }

        private fun errorProgress(e: Exception): ProgressResult =
            ProgressResult().also { it.exception = e }

        fun getDefaultValue(returnType: Type, e: Exception): Any {
            return when (returnType) {
                Void.TYPE -> 0
                DownloadState::class -> errorDownload(e)
                ProgressResult::class -> errorProgress(e)
                Result::class -> Result(e)
                else -> throw e
            }
        }
    }
}