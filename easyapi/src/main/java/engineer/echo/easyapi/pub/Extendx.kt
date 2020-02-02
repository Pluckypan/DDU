package engineer.echo.easyapi.pub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.EasyLiveData
import engineer.echo.easyapi.download.DownloadHelper.downloadId
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
    var id = easyId()
    if (id.isEmpty()) {
        id = downloadId()
    }
    if (id.isEmpty()) return
    this.value?.let {
        if (it is DownloadState) {
            EasyApi.cancelDownload(id)
        } else {
            EasyApi.cancel(id)
        }
    }
}

/**
 * 将下载状态转发给新的 MutableLiveData
 */
fun LiveData<DownloadState>.assignTo(liveData: MutableLiveData<DownloadState>) {
    observeForever {
        liveData.value = it
    }
}