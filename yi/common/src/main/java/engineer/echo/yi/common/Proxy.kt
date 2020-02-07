package engineer.echo.yi.common

import android.content.Context
import engineer.echo.easyapi.annotation.JobApi

object Proxy {

    const val KEY_CONSUMER = "@EasyProxy:Consumer"
    const val KEY_IM = "@EasyProxy:IM"
    const val KEY_LIVE = "@EasyProxy:Live"
    const val KEY_PRODUCER = "@EasyProxy:Producer"

    @JobApi(uniqueId = KEY_CONSUMER)
    interface ConsumerApi {

        fun goto(context: Context)
    }

    @JobApi(uniqueId = KEY_IM)
    interface IMApi {
        fun goto(context: Context)
    }

    @JobApi(uniqueId = KEY_LIVE)
    interface LiveApi {
        fun goto(context: Context)
    }

    @JobApi(uniqueId = KEY_PRODUCER)
    interface ProducerApi {
        fun goto(context: Context)
    }
}