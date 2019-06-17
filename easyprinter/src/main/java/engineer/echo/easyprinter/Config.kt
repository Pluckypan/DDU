package engineer.echo.easyprinter

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.SystemClock
import engineer.echo.easylib.formatLog
import engineer.echo.easyprinter.strategy.PrintStrategy
import java.util.*

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
        val UNIQUE_ID: UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB")

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

        fun Int.deviceType(): String {
            return when (this) {
                BluetoothDevice.DEVICE_TYPE_CLASSIC -> "classic"
                BluetoothDevice.DEVICE_TYPE_LE -> "le"
                BluetoothDevice.DEVICE_TYPE_DUAL -> "dual"
                BluetoothDevice.DEVICE_TYPE_UNKNOWN -> "unknown"
                else -> "*unknown"
            }
        }

        fun Int.majorClass(): String {
            return when (this) {
                BluetoothClass.Device.Major.MISC -> "misc"
                BluetoothClass.Device.Major.COMPUTER -> "computer"
                BluetoothClass.Device.Major.PHONE -> "phone"
                BluetoothClass.Device.Major.NETWORKING -> "networking"
                BluetoothClass.Device.Major.AUDIO_VIDEO -> "audio_video"
                BluetoothClass.Device.Major.PERIPHERAL -> "peripheral"
                BluetoothClass.Device.Major.IMAGING -> "imaging" //打印机
                BluetoothClass.Device.Major.WEARABLE -> "wearable"
                BluetoothClass.Device.Major.TOY -> "toy"
                BluetoothClass.Device.Major.HEALTH -> "health"
                BluetoothClass.Device.Major.UNCATEGORIZED -> "uncategorized"
                else -> "*uncategorized"

            }
        }

        fun BluetoothSocket.writeData(vararg dataList: ByteArray) {
            try {
                val before = SystemClock.uptimeMillis()
                this.outputStream.let { os ->
                    dataList.forEach { data ->
                        os.write(data)
                    }
                    os.flush()
                }
                "writeData cost=%s".formatLog(TAG, (SystemClock.uptimeMillis() - before))
            } catch (e: Exception) {
                "writeData failed.%s".formatLog(TAG, e.message)
            }
        }
    }

    lateinit var strategy: PrintStrategy
}