package engineer.echo.yi.consumer.cmpts.weibo

import android.app.Activity
import android.app.Application
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo


object Weibo {

    internal const val TAG = "EasyWeibo"
    internal lateinit var application: Application
    internal var debugMode = false
    internal const val APP_KEY = "2930073200"
    private const val REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"
    private const val APP_SCOPE =
        "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"

    /**
     * on Application onCreate
     */
    fun install(app: Application, debug: Boolean = false) {
        application = app
        this.debugMode = debug
        val auth = AuthInfo(app, APP_KEY, REDIRECT_URL, APP_SCOPE)
        WbSdk.install(app, auth)
    }

    /**
     * @param activity your LoginActivity
     * @return WeiboAuthHandler
     * & please call [WeiboAuthHandler.authorizeCallBack] on your LoginActivity [Activity.onActivityResult]
     *
     */
    fun authorize(activity: Activity): WeiboAuthHandler {
        return WeiboAuthHandler(activity).apply {
            authorize(AuthListener())
        }
    }

    /**
     * get weibo token
     */
    fun readToken(): String = Helper.readToken()

    /**
     * logout
     */
    fun clearToken() = Helper.clearToken()

    /**
     * refresh token
     */
    fun refreshToken(action: ((success: Boolean, token: String, error: Exception?) -> Unit)? = null) =
        Helper.refreshToken(action)

}