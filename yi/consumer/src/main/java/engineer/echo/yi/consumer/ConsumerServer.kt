package engineer.echo.yi.consumer

import android.content.Context
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.common.Proxy
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.ui.main.MainActivity

@JobServer(uniqueId = Proxy.KEY_CONSUMER)
class ConsumerServer : Proxy.ConsumerApi {

    companion object {
        val params by lazy {
            HashMap<String, String>().apply {
                put("access_token", Weibo.readToken())
            }
        }
    }

    override fun goto(context: Context) {
        MainActivity.goto(context)
    }

    override fun onAppInit(app: EasyApp) {
        ConsumerApp.onAppInit(app)
    }

    override fun getRequestParams(url: String, method: String): HashMap<String, String>? {
        return params.apply {
            if (get("access_token").isNullOrBlank()) {
                put("access_token", Weibo.readToken())
            }
        }
    }
}