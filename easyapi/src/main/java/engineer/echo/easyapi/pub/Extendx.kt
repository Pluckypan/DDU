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
 * LiveData 状态转发
 * 在 LiveData 的世界里,一切都是由 LiveData 驱动,让 triggerLiveData 去触发 其他 LiveData 产生
 * 详情参考 Yi ApiMockViewModel#refreshTrigger
 */
@Deprecated(
    "please use triggerLiveData",
    ReplaceWith("Transformations.switchMap(triggerLiveData) { if(it) LiveData1 else LiveData2  }")
)
fun <T> LiveData<T>.assignTo(liveData: MutableLiveData<T>) {
    observeForever {
        liveData.value = it
    }
}