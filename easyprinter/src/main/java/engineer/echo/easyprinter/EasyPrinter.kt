package engineer.echo.easyprinter

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import engineer.echo.easyprinter.strategy.DefaultFactory
import engineer.echo.easyprinter.strategy.StrategyFactory

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
        }

        fun get(): EasyPrinter {
            return sPrinter
        }
    }

    private val mBlueAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mMonitor: Monitor
    private lateinit var mConfig: Config

    /**
     * 初始化
     */
    fun setup(application: Application, factory: StrategyFactory? = null): EasyPrinter {
        mConfig = Config(application).apply {
            strategy = factory?.create() ?: DefaultFactory().create()
        }.also {
            mMonitor = Monitor(it.strategy)
            it.application.registerReceiver(mMonitor, sFilter)
        }
        return this
    }

    /**
     * 释放
     */
    fun release() {
        mConfig.apply {
            application.unregisterReceiver(mMonitor)
        }
    }

    fun enable(): Boolean {
        return mBlueAdapter.enable()
    }

    fun disable(): Boolean {
        return mBlueAdapter.disable()
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

    fun createBondTo(device: BluetoothDevice): Boolean {
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