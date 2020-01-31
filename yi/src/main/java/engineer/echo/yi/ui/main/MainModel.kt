package engineer.echo.yi.ui.main

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easylib.externalCache
import engineer.echo.yi.YiApp
import java.io.File

class MainModel : MainContract.IModel {

    override fun download(): LiveData<DownloadState> {
        return EasyApi.download(
            APK_URL,
            File(YiApp.getApp().externalCache(), "2019-07-18.apk").path
        )
    }

    companion object {
        private const val APK_URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"
    }
}