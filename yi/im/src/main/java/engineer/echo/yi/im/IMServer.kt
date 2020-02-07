package engineer.echo.yi.im

import android.content.Context
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.Proxy

@JobServer(uniqueId = Proxy.KEY_IM)
class IMServer : Proxy.IMApi {

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }
}