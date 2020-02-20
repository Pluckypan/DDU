package engineer.echo.yi.consumer.cmpts.weibo

import android.util.Log
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener

internal object Helper {

    internal fun printLog(format: String, vararg args: Any?) {
        if (Weibo.debugMode) {
            Log.i(
                Weibo.TAG,
                format.format(*args).plus(" thread=[${Thread.currentThread().id}:${Thread.currentThread().name}]")
            )
        }
    }

    fun writeToken(token: Oauth2AccessToken) {
        if (token.isSessionValid) {
            AccessTokenKeeper.writeAccessToken(Weibo.application, token)
        }
    }

    fun readToken(): String = AccessTokenKeeper.readAccessToken(Weibo.application).token

    fun clearToken() = AccessTokenKeeper.clear(Weibo.application)

    fun refreshToken(action: ((success: Boolean, token: String, error: Exception?) -> Unit)? = null) {

        AccessTokenKeeper.refreshToken(Weibo.APP_KEY, Weibo.application, object : RequestListener {
            override fun onWeiboException(exception: WeiboException?) {
                printLog("refreshToken onWeiboException error = %s", exception)
                action?.invoke(false, "", exception)
            }

            override fun onComplete(resp: String?) {
                printLog("refreshToken onComplete resp = %s", resp)
                action?.invoke(true, readToken(), null)
            }

        })
    }
}