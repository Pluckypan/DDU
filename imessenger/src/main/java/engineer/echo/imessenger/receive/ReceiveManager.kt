package engineer.echo.imessenger.receive

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.printLine

/**
 *  ReceiveManager.kt
 *  Info: 消息接收处理
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/10 - 10:02 PM
 *  more about me: http://www.1991th.com
 */
abstract class ReceiveManager : Service() {

    companion object {
        private const val TAG = "ReceiveManager"
    }

    private val mReceiver = Messenger(this.obtainHandler())

    override fun onBind(intent: Intent?): IBinder? {
        return mReceiver.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "onUnbind".printLine(TAG)
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        "onRebind".printLine(TAG)
        super.onRebind(intent)
    }

    abstract fun obtainHandler(): Handler

    override fun onCreate() {
        "onCreate".printLine(TAG)
        super.onCreate()
    }

    override fun onDestroy() {
        "onDestroy".printLine(TAG)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "onStartCommand %d %d".formatLog(TAG, flags, startId)
        return super.onStartCommand(intent, flags, startId)
    }
}