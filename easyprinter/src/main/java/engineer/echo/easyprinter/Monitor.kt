package engineer.echo.easyprinter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 *  Monitor.kt
 *  Info: 蓝牙状态接收
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 10:05 AM
 *  more about me: http://www.1991th.com
 */
class Monitor(private val listener: IMonitor? = null) : BroadcastReceiver(), IMonitor {

    override fun onReceive(context: Context, intent: Intent) {

    }

    override fun onStartDiscovery(intent: Intent) {
        listener?.onStartDiscovery(intent)
    }

    override fun onFinishDiscovery(intent: Intent) {
        listener?.onFinishDiscovery(intent)
    }

    override fun onLocalStateChanged(state: Int, previousState: Int) {
        listener?.onLocalStateChanged(state, previousState)
    }
}