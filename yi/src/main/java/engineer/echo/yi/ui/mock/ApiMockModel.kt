package engineer.echo.yi.ui.mock

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easylib.externalCache
import engineer.echo.yi.YiApp
import java.io.File

class ApiMockModel : ApiMockContract.IModel {


    override fun download(apk: Boolean): LiveData<DownloadState> {
        return EasyApi.download(if (apk) APK_URL else IMG_URL, getPath())
    }

    companion object {
        private const val APK_URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"
        private const val IMG_URL = "http://img.1991th.com/tuchongeter/girls/4af800008b65ef6bfb75"
        private fun getPath(): String = File(YiApp.getApp().externalCache(), "2019-07-18").path
    }
}