package engineer.echo.easyprinter

import android.app.Application
import android.app.IntentService
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.printLine
import engineer.echo.easyprinter.Config.Companion.TAG

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

        fun stop(application: Application) {
            application.stopService(Intent(application, PrinterService::class.java))
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.apply {
            // IntentService 会顺序执行完发送过来的任务
            val task = getIntExtra(KEY_TASK_TYPE, 0)
            "onHandleIntent thread=%s task=%d".formatLog(TAG, Thread.currentThread().name, task)
            when (task) {
                TASK_PRINT -> {
                    val data = getByteArrayExtra(KEY_DATA)
                    val device = getParcelableExtra<BluetoothDevice>(KEY_DEVICE)
                    print(device, data)
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
        EasyPrinter.get().createSocket(device)?.use {
            if (!it.isConnected) {
                try {
                    it.connect()
                } catch (e: Exception) {
                    "print connect failed.%s".formatLog(TAG, e.message)
                    return
                }
            }
            try {
                it.outputStream.use { os ->
                    os.write(data)
                    os.write(Config.print)
                    os.flush()
                }
            } catch (e: Exception) {
                "print write failed.%s".formatLog(TAG, e.message)
            }
        }
    }

}