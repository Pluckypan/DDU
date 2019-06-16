package engineer.echo.easyprinter

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
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

        val left = byteArrayOf(0x1b, 0x61, 0x00)// 靠左
        val center = byteArrayOf(0x1b, 0x61, 0x01)// 居中
        val right = byteArrayOf(0x1b, 0x61, 0x02)// 靠右
        val bold = byteArrayOf(0x1b, 0x45, 0x01)// 选择加粗模式
        val bold_cancel = byteArrayOf(0x1b, 0x45, 0x00)// 取消加粗模式
        val text_normal_size = byteArrayOf(0x1d, 0x21, 0x00)// 字体不放大
        val text_big_height = byteArrayOf(0x1b, 0x21, 0x10)// 高加倍
        val text_big_size = byteArrayOf(0x1d, 0x21, 0x11)// 宽高加倍
        val reset = byteArrayOf(0x1b, 0x40)//复位打印机
        val print = byteArrayOf(0x0a)//打印并换行
        val under_line = byteArrayOf(0x1b, 0x2d, 2)//下划线
        val under_line_cancel = byteArrayOf(0x1b, 0x2d, 0)//下划线

        /**
         * 走纸
         *
         * @param n 行数
         * @return 命令
         */
        fun walkPaper(n: Byte): ByteArray {
            return byteArrayOf(0x1b, 0x64, n)
        }

        /**
         * 设置横向和纵向移动单位
         *
         * @param x 横向移动
         * @param y 纵向移动
         * @return 命令
         */
        fun move(x: Byte, y: Byte): ByteArray {
            return byteArrayOf(0x1d, 0x50, x, y)
        }
    }

    lateinit var strategy: PrintStrategy
}