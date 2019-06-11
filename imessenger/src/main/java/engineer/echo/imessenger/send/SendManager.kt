package engineer.echo.imessenger.send

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import engineer.echo.easylib.Core.formatLog
import engineer.echo.easylib.Core.printLine

/**
 *  SendManager.ktr.kt
 *  Info: 消息发送服务
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/10 - 10:32 PM
 *  more about me: http://www.1991th.com
 */
abstract class SendManager : Service(), ServiceConnection {

    companion object {
        private const val TAG = "SendManager"
        private var sBinder: SenderBinder? = null
        private val sConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                sBinder = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                if (service is SenderBinder) {
                    sBinder = service
                }
            }
        }

        fun register(context: Context) {
            Intent(context, SendManager::class.java).also {
                context.bindService(it, sConnection, Context.BIND_AUTO_CREATE)
                "register".printLine(TAG)
            }
        }

        fun unregister(context: Context) {
            try {
                context.unbindService(sConnection)
                "unregister".printLine(TAG)
            } catch (e: Exception) {
                "unregister %s".formatLog(TAG, e.message)
            }
        }

        fun getSenderManager(): SendManager? {
            return sBinder?.getService()
        }
    }

    private var mReplyMessenger = Messenger(this.obtainHandler())
    private var mSender: Messenger? = null

    abstract fun obtainHandler(): Handler
    abstract fun obtainPackageName(): String
    abstract fun obtainServiceName(): String

    override fun onBind(intent: Intent?): IBinder? {
        "onBind".printLine(TAG)
        return SenderBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "onUnbind".printLine(TAG)
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        "onRebind".printLine(TAG)
        super.onRebind(intent)
    }

    private inner class SenderBinder : Binder() {

        fun getService(): SendManager {
            return this@SendManager
        }
    }

    override fun onCreate() {
        "onCreate".printLine(TAG)
        super.onCreate()
        startConnection()
    }

    override fun onDestroy() {
        "onDestroy".printLine(TAG)
        stopConnection()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "onStartCommand %d %d".formatLog(TAG, flags, startId)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startConnection() {
        "startConnection".printLine(TAG)
        Intent().apply {
            setClassName(obtainPackageName(), obtainServiceName())
        }.also {
            bindService(it, this, Context.BIND_AUTO_CREATE)
        }
    }

    private fun stopConnection() {
        try {
            unbindService(this)
            "stopConnection".printLine(TAG)
        } catch (e: Exception) {
            "stopConnection error:%s".formatLog(TAG, e.message)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        "onServiceDisconnected %s %s".formatLog(TAG, name?.packageName, name?.shortClassName)
        mSender = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        "onServiceConnected %s %s".formatLog(TAG, name?.packageName, name?.shortClassName)
        service?.let {
            mSender = Messenger(it)
        }
    }

    fun sendMessage(bundle: Bundle) {
        mSender?.let { messenger ->
            Message.obtain().apply {
                data = bundle
                replyTo = mReplyMessenger
            }.also {
                try {
                    messenger.send(it)
                    "sendMessage".printLine(TAG)
                } catch (e: Exception) {
                    "sendMessage %s".formatLog(TAG, e.message)
                }
            }
        }
    }
}