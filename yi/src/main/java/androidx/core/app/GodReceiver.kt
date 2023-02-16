package androidx.core.app

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class GodReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Plucky", "onReceive intent=$intent")
    }

    companion object {

        private val receiver by lazy {
            GodReceiver()
        }

        fun register(activity: Activity) {
            val intentFilter = IntentFilter()
            intentFilter.addAction("com.android.systemui.screenshot.SHOW_ERROR")
            intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION")
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED")
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            intentFilter.addAction(Intent.ACTION_TIME_TICK)
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
            intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS")
            intentFilter.addAction("android.intent.action.SHOW_SUSPENDED_APP_DETAILS")
            intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED")
            intentFilter.addAction("oplus.intent.action.SCREENSHOT_SETTINGS")
            intentFilter.addAction("com.oplus.screenshot.depends.activity.NOTIFY")
            intentFilter.addAction("com.oplus.screenshot.editor.ACTION_SHOT_EDIT")
            intentFilter.addAction("android.intent.action.USER_PRESENT")


            activity.registerReceiver(receiver, intentFilter)
        }

        fun unregister(activity: Activity) {
            activity.unregisterReceiver(receiver)
        }
    }
}