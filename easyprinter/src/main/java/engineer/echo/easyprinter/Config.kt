package engineer.echo.easyprinter

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import engineer.echo.easyprinter.strategy.PrintStrategy

/**
 *  Config.kt
 *  Info: 蓝牙配置
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 12:14 AM
 *  more about me: http://www.1991th.com
 */
class Config(
    val application: Application
) {
    companion object {
        const val TAG = "EasyPrinter"
        fun Int.localState(): String {
            return when (this) {
                BluetoothAdapter.STATE_ON -> "on"
                BluetoothAdapter.STATE_TURNING_ON -> "turning on"
                BluetoothAdapter.STATE_TURNING_OFF -> "turning off"
                BluetoothAdapter.STATE_OFF -> "off"
                else -> {
                    "*off"
                }
            }
        }

        fun Int.connectionState(): String {
            return when (this) {
                BluetoothAdapter.STATE_CONNECTED -> "connected"
                BluetoothAdapter.STATE_CONNECTING -> "connecting"
                BluetoothAdapter.STATE_DISCONNECTING -> "disconnecting"
                BluetoothAdapter.STATE_DISCONNECTED -> "disconnected"
                else -> "*disconnected"
            }
        }

        fun Int.bondState(): String {
            return when (this) {
                BluetoothDevice.BOND_NONE -> "none"
                BluetoothDevice.BOND_BONDED -> "bonded"
                BluetoothDevice.BOND_BONDING -> "bonding"
                else -> "*none"
            }
        }
    }
    lateinit var strategy: PrintStrategy
}