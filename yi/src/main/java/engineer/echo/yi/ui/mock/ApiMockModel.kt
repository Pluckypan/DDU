package engineer.echo.yi.ui.mock

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.common.Proxy

class ApiMockModel : ApiMockContract.IModel {


    override fun download(apk: Boolean): LiveData<DownloadState> {
        return EasyApi.download(APK_URL, zipPath)
    }

    override fun zipAction(zip: Boolean): LiveData<Result> {
        return EasyApi.getProxy(Proxy.ProducerApi::class.java).let {
            if (zip) it.zip(unzipFolder, zipPath) else it.unzip(zipPath, unzipFolder)
        }
    }

    companion object {
        private const val APK_URL =
            "http://img.1991.wiki/llyf.zip"

        private val zipPath by lazy {
            EasyApi.getProxy(Proxy.ProducerApi::class.java).getZipPath()
        }

        private val unzipFolder by lazy {
            EasyApi.getProxy(Proxy.ProducerApi::class.java).getUnzipFolder()
        }
    }
}