package engineer.echo.easyprinter

import android.app.Application
import android.app.IntentService
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.memInfo
import engineer.echo.easylib.printLine
import engineer.echo.easyprinter.Config.Companion.TAG
import engineer.echo.easyprinter.Config.Companion.writeData
import engineer.echo.easyprinter.command.CommandBox

/**
 *  PrinterService.kt
 *  Info: 蓝牙打印服务
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 8:45 PM
 *  more about me: http://www.1991th.com
 */
class PrinterService @JvmOverloads constructor(name: String = TAG) : IntentService(name) {

    companion object {

        private const val KEY_TASK_TYPE = "key_for_task_type"
        private const val TASK_PRINT = 1
        private const val KEY_DEVICE = "key_for_print_device"
        private const val KEY_DATA = "key_for_print_data"
        private const val TASK_CONNECT = 2

        fun print(
            application: Application,
            device: BluetoothDevice,
            data: ByteArray
        ) {
            Intent(application, PrinterService::class.java).apply {
                putExtra(KEY_TASK_TYPE, TASK_PRINT)
                putExtra(KEY_DEVICE, device)
                putExtra(KEY_DATA, data)
            }.also {
                application.startService(it)
            }
        }

        fun connect(
            application: Application,
            device: BluetoothDevice
        ) {
            Intent(application, PrinterService::class.java).apply {
                putExtra(KEY_TASK_TYPE, TASK_CONNECT)
                putExtra(KEY_DEVICE, device)
            }.also {
                application.startService(it)
            }
        }

        fun stop(application: Application) {
            application.stopService(Intent(application, PrinterService::class.java))
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.apply {
            // IntentService 会顺序执行完发送过来的任务
            val task = getIntExtra(KEY_TASK_TYPE, 0)
            "onHandleIntent thread=(%s,%s) task=%d".formatLog(
                TAG,
                Thread.currentThread().name,
                Thread.currentThread().id,
                task
            )
            when (task) {
                TASK_PRINT -> {
                    val data = getByteArrayExtra(KEY_DATA) ?: return
                    val device = getParcelableExtra<BluetoothDevice>(KEY_DEVICE) ?: return
                    print(device, data)
                }
                TASK_CONNECT -> {
                    val device = getParcelableExtra<BluetoothDevice>(KEY_DEVICE) ?: return
                    connectTo(device)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        "onCreate".printLine(TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "onStartCommand %d %d".formatLog(TAG, flags, startId)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".printLine(TAG)
    }

    override fun onBind(intent: Intent?): IBinder? {
        "onBind".printLine(TAG)
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "onUnbind".printLine(TAG)
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        "onRebind".printLine(TAG)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        "onTaskRemoved".printLine(TAG)
    }

    private fun print(device: BluetoothDevice, data: ByteArray) {
        val before = SystemClock.uptimeMillis()
        EasyPrinter.get().createSocket(device)?.let {
            "print createSocket %s %s".formatLog(TAG, it.memInfo(), it.isConnected)
            if (!it.isConnected) {
                EasyPrinter.get().connectTo(device)
            }
            it.writeData(CommandBox.RESET)
            it.writeData(data)
        }
        "print(%s) size=%s cost=%s".formatLog(
            TAG,
            device.address,
            data.size,
            (SystemClock.uptimeMillis() - before)
        )
    }

    private fun connectTo(device: BluetoothDevice) {
        EasyPrinter.get().connectTo(device)
    }

}