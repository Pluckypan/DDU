package engineer.echo.yi.ui.mock

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easylib.externalCache
import engineer.echo.yi.cmpts.zip.ZipHelper
import engineer.echo.yi.common.EasyApp
import java.io.File

class ApiMockModel : ApiMockContract.IModel {


    override fun download(apk: Boolean): LiveData<DownloadState> {
        return EasyApi.download(if (apk) APK_URL else IMG_URL, getPath())
    }

    override fun zipAction(zip: Boolean): LiveData<Result> {
        return if (zip) {
            EasyApi.create(ZipHelper.ZipApi::class.java).zip(getPath(), getZip())
        } else {
            EasyApi.create(ZipHelper.ZipApi::class.java).unzip(
                getZip(),
                unzipFolder
            ) as LiveData<Result>
        }
    }

    companion object {
        private const val APK_URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"
        private const val IMG_URL = "http://img.1991th.com/tuchongeter/girls/4af800008b65ef6bfb75"

        private fun getPath(): String = File(EasyApp.getApp().externalCache(), "2019-07-18").path
        private fun getZip(): String = getPath().plus("-job.zip")
        private val unzipFolder = File(EasyApp.getApp().externalCache(), "job").path
    }
}