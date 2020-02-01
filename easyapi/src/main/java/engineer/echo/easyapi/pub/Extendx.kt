package engineer.echo.easyapi.pub

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyLiveData
import engineer.echo.easyapi.download.DownloadState
import java.io.File

fun File.tryCreateFileException(): Exception? {
    if (!exists()) {
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        try {
            createNewFile()
        } catch (e: Exception) {
            return e
        }
    }
    return null
}

fun File.tryCreateFile(): Boolean {
    return tryCreateFileException() == null
}

fun LiveData<*>.easyId(): String {
    return if (this is EasyLiveData) id else ""
}

fun LiveData<*>.cancelRequest() {
    val id = easyId()
    if (id.isEmpty()) return
    this.value?.let {
        if (it.javaClass == DownloadState::javaClass) {
            EasyApi.cancelDownload(id)
        } else {
            EasyApi.cancel(id)
        }
    }
}