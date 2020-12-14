package engineer.echo.yi.common.cpmts

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * EasyScope
 * Created by Plucky<plucky@echo.engineer> on 12/14/20 10:37 PM.
 * more about me: http://www.1991th.com
 */

private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

val workerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

val uiScope
    get() = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)