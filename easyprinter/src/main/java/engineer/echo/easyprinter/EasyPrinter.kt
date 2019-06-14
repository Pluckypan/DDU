package engineer.echo.easyprinter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter

/**
 *  EasyPrinter.kt
 *  Info: 蓝牙打印机
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/13 - 10:28 PM
 *  more about me: http://www.1991th.com
 */
class EasyPrinter private constructor() {

    companion object {
        private val sPrinter by lazy {
            // 线程安全 & 双重校验 & 懒汉式
            EasyPrinter()
        }

        private val sFilter = IntentFilter().apply {
            // 开启扫描
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            // 结束扫描
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            // 本机蓝牙状态
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            // 蓝牙连接状态
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            // 本机蓝牙名称修改
            addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)
            // 扫描结果
            addAction(BluetoothDevice.ACTION_FOUND)
            // 蓝牙绑定状态
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            // 配对请求结果
            addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        }

        fun get(): EasyPrinter {
            return sPrinter
        }
    }

    private val mBlueAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mConfig: Config

    fun setup(config: Config): EasyPrinter {
        this.mConfig = config
        return this
    }

    fun enable(): Boolean {
        return mBlueAdapter.enable()
    }

    fun open(): Boolean {
        if (mBlueAdapter.isEnabled) {
            return true
        }
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
            mConfig.application.startActivity(it)
        }
        return false
    }

    fun isEnabled(): Boolean {
        return mBlueAdapter.isEnabled
    }

    fun isDiscovering(): Boolean {
        return mBlueAdapter.isDiscovering
    }

    fun startDiscovery(): Boolean {
        return mBlueAdapter.startDiscovery()
    }

    fun cancelDiscovery(): Boolean {
        return mBlueAdapter.cancelDiscovery()
    }

    fun connectTo(device: BluetoothDevice): Boolean {
        return device.createBond()
    }

    fun getBondedDevices(): Set<BluetoothDevice> {
        return mBlueAdapter.bondedDevices
    }

    fun getLocalName(): String {
        return mBlueAdapter.name ?: ""
    }

    @SuppressLint("HardwareIds")
    fun getLocalAddress(): String {
        return mBlueAdapter.address ?: ""
    }
}