package engineer.echo.easyprinter

import android.content.Intent

/**
 *  IMonitor.kt
 *  Info: 蓝牙状态接口
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 10:06 AM
 *  more about me: http://www.1991th.com
 */
interface IMonitor {
    fun onStartDiscovery(intent: Intent)
    fun onFinishDiscovery(intent: Intent)
    /**
     * @param state  当前状态
     * @param previousState 上一次状态
     * [BluetoothAdapter#STATE_OFF]
     * [BluetoothAdapter#STATE_TURNING_ON]
     * [BluetoothAdapter#STATE_ON]
     * [BluetoothAdapter#STATE_TURNING_OFF]
     */
    fun onLocalStateChanged(state: Int, previousState: Int)
}