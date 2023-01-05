package engineer.echo.yi.common.cpmts

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*

/**
 * EasyScope
 * Created by Plucky<plucky@echo.engineer> on 12/14/20 10:37 PM.
 * more about me: http://www.1991th.com
 */

private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

val workerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

val Lifecycle.uiScope
    get() = this.coroutineScope + SupervisorJob() + Dispatchers.Main + exceptionHandler

val currentThreadName: String
    get() = Thread.currentThread().name