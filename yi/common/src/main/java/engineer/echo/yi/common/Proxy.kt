package engineer.echo.yi.common

import android.content.Context
import androidx.lifecycle.LiveData
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobApi

object Proxy {

    const val KEY_CONSUMER = "@EasyProxy:Consumer"
    const val KEY_IM = "@EasyProxy:IM"
    const val KEY_LIVE = "@EasyProxy:Live"
    const val KEY_PRODUCER = "@EasyProxy:Producer"

    @JobApi(uniqueId = KEY_CONSUMER)
    interface ConsumerApi {

        fun onAppInit(app:EasyApp)

        fun goto(context: Context)

        fun getRequestParams(url: String, method: String): HashMap<String, String>?
    }

    @JobApi(uniqueId = KEY_IM)
    interface IMApi {
        fun onAppInit(app:EasyApp)
        fun goto(context: Context)
    }

    @JobApi(uniqueId = KEY_LIVE)
    interface LiveApi {
        fun onAppInit(app:EasyApp)
        fun goto(context: Context)
    }

    @JobApi(uniqueId = KEY_PRODUCER)
    interface ProducerApi {

        fun onAppInit(app:EasyApp)

        fun goto(context: Context)

        fun zip(source: String, target: String): LiveData<Result>

        fun unzip(source: String, target: String): LiveData<Result>

        fun getZipPath(): String

        fun getUnzipFolder(): String
    }
}