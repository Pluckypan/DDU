package engineer.echo.yi.producer

import android.content.Context
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.Proxy

@JobServer(uniqueId = Proxy.KEY_PRODUCER)
class ProducerServer : Proxy.ProducerApi {

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }
}