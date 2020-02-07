package engineer.echo.yi.consumer

import android.content.Context
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.Proxy

@JobServer(uniqueId = Proxy.KEY_CONSUMER)
class ConsumerServer : Proxy.ConsumerApi {

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }
}