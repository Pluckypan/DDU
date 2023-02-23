package engineer.echo.easylib

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.ycbjie.notificationlib.NotificationUtils

object NotifierHelper {

    private const val DEFAULT_CHANNEL_ID = "EasyApp"
    private const val SILENT_CHANNEL_ID = "silent"
    private lateinit var application: Application

    private const val NO1 = 0x1001
    private const val NO2 = 0x1002
    private const val NO3 = 0x1003
    private const val NO4 = 0x1004

    fun init(app: Application) {
        application = app
    }

    private val notifyManager by lazy {
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notifyManagerCompat by lazy {
        NotificationManagerCompat.from(application)
    }

    @JvmStatic
    val notificationPermissionEnable: Boolean
        get() = notifyManagerCompat.areNotificationsEnabled().also {
            Log.i("NotifierHelper", "notificationPermissionEnable = $it")
        }

    /**
     * 默认推送
     */
    @JvmStatic
    val defaultChannelId: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                DEFAULT_CHANNEL_ID,
                DEFAULT_CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            ).also {
                notifyManager.createNotificationChannel(it)
            }.id
        } else {
            DEFAULT_CHANNEL_ID
        }
    }

    /**
     * 静默推送
     */
    @JvmStatic
    val silenceChannelId: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                SILENT_CHANNEL_ID,
                SILENT_CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
            ).also {
                notifyManager.createNotificationChannel(it)
            }.id
        } else {
            SILENT_CHANNEL_ID
        }
    }

    fun init() {
        Log.i("Plucky", "$defaultChannelId - $silenceChannelId")
    }

    fun simple(icon: Int, title: String, content: String, summary: String) {
        NotificationUtils(application, defaultChannelId, "EasyInc")
            .sendNotification(NO1, title, content, icon)
    }
}