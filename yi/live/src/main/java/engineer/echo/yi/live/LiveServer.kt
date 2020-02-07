package engineer.echo.yi.live

import android.content.Context
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.Proxy

@JobServer(uniqueId = Proxy.KEY_LIVE)
class LiveServer:Proxy.LiveApi {

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }

}