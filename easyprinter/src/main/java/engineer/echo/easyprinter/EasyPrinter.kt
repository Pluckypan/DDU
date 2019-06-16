package engineer.echo.easyprinter

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.IntentFilter
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import engineer.echo.easyprinter.entity.BondEntity
import engineer.echo.easyprinter.entity.ConnectionEntity
import engineer.echo.easyprinter.entity.DiscoveryEntity
import engineer.echo.easyprinter.entity.LocalStateEntity
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
    private val mDiscoveryLiveData = MutableLiveData<DiscoveryEntity>()
    private val mLocalStateLiveData = MutableLiveData<LocalStateEntity>()
    private val mLocalNameLiveData = MutableLiveData<String>()
    private val mConnectionLiveData = MutableLiveData<ConnectionEntity>()
    private val mBondLiveData = MutableLiveData<BondEntity>()

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

    fun postDiscovery(entity: DiscoveryEntity) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mDiscoveryLiveData.value = entity
        } else {
            mDiscoveryLiveData.postValue(entity)
        }
    }

    @MainThread
    fun observeDiscovery(owner: LifecycleOwner, observer: Observer<DiscoveryEntity>) {
        mDiscoveryLiveData.observe(owner, observer)
    }

    fun postLocalState(state: LocalStateEntity) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mLocalStateLiveData.value = state
        } else {
            mLocalStateLiveData.postValue(state)
        }
    }

    @MainThread
    fun observeLocalState(owner: LifecycleOwner, observer: Observer<LocalStateEntity>) {
        mLocalStateLiveData.observe(owner, observer)
    }

    fun postLocalName(name: String) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mLocalNameLiveData.value = name
        } else {
            mLocalNameLiveData.postValue(name)
        }
    }

    @MainThread
    fun observeLocalName(owner: LifecycleOwner, observer: Observer<String>) {
        mLocalNameLiveData.observe(owner, observer)
    }

    fun postConnection(entity: ConnectionEntity) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mConnectionLiveData.value = entity
        } else {
            mConnectionLiveData.postValue(entity)
        }
    }

    @MainThread
    fun observeConnection(owner: LifecycleOwner, observer: Observer<ConnectionEntity>) {
        mConnectionLiveData.observe(owner, observer)
    }

    fun postBond(entity: BondEntity) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            mBondLiveData.value = entity
        } else {
            mBondLiveData.postValue(entity)
        }
    }

    @MainThread
    fun observeBond(owner: LifecycleOwner, observer: Observer<BondEntity>) {
        mBondLiveData.observe(owner, observer)
    }

    fun print(device: BluetoothDevice, data: ByteArray) {
        PrinterService.print(mConfig.application, device, data)
    }

    fun stopService() {
        PrinterService.stop(mConfig.application)
    }

    fun createSocket(device: BluetoothDevice): BluetoothSocket? {
        return try {
            device.createRfcommSocketToServiceRecord(Config.UNIQUE_ID)
        } catch (e: Exception) {
            null
        }
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

    fun isBonded(device: BluetoothDevice): Boolean {
        return device.bondState == BluetoothDevice.BOND_BONDED
    }

    fun isBonding(device: BluetoothDevice): Boolean {
        return device.bondState == BluetoothDevice.BOND_BONDING
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