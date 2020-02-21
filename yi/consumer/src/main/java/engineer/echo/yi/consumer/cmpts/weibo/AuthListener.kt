package engineer.echo.yi.consumer.cmpts.weibo

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage

internal class AuthListener : WbAuthListener {

    override fun onSuccess(token: Oauth2AccessToken) {
        Helper.printLog(
            "AuthListener onSuccess [valid=%s] token = %s uid = %s",
            token.isSessionValid,
            token.token,
            token.uid
        )
        Helper.writeToken(token)

    }

    override fun onFailure(error: WbConnectErrorMessage) {
        Helper.printLog("AuthListener onFailure error = %s", error)
    }

    override fun cancel() {
        Helper.printLog("AuthListener cancel")
    }

}