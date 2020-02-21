package engineer.echo.yi.producer.cmpts

import android.content.Context
import androidx.lifecycle.LiveData
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.common.Proxy
import engineer.echo.yi.producer.ProducerApp
import engineer.echo.yi.producer.cmpts.zip.ZipApiRetrofit
import engineer.echo.yi.producer.ui.main.MainActivity
import engineer.echo.yi.producer.ui.main.MainViewModel

@JobServer(uniqueId = Proxy.KEY_PRODUCER)
class ProducerServer : Proxy.ProducerApi {

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }

    override fun zip(source: String, target: String): LiveData<Result> =
        EasyApi.create(ZipApiRetrofit::class.java).zip(source, target, 1, true, 1f, 1)

    override fun unzip(source: String, target: String): LiveData<Result> =
        EasyApi.create(ZipApiRetrofit::class.java).unzip(source, target)

    override fun getZipPath(): String = MainViewModel.zipPath

    override fun getUnzipFolder(): String = MainViewModel.unzipFolder

    override fun onAppInit(app: EasyApp) {
        ProducerApp.onAppInit(app)
    }
}