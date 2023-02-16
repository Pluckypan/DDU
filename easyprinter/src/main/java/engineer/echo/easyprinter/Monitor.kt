package engineer.echo.easyprinter

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.printLine
import engineer.echo.easyprinter.Config.Companion.TAG
import engineer.echo.easyprinter.Config.Companion.bondState
import engineer.echo.easyprinter.Config.Companion.connectionState
import engineer.echo.easyprinter.Config.Companion.deviceType
import engineer.echo.easyprinter.Config.Companion.localState
import engineer.echo.easyprinter.Config.Companion.majorClass
import engineer.echo.easyprinter.entity.*
import engineer.echo.easyprinter.strategy.PrintStrategy

/**
 *  Monitor.kt
 *  Info: 蓝牙状态接收
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 10:05 AM
 *  more about me: http://www.1991th.com
 */
class Monitor(
    private val strategy: PrintStrategy
) : BroadcastReceiver(),
    IMonitor {

    private val listener: IMonitor? = strategy.getGlobalMonitor()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    onStartDiscovery(intent)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    onFinishDiscovery(intent)
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    onLocalStateChanged(
                        intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF),
                        intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_OFF)
                    )
                }
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val state =
                        intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED)
                    val preState = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE,
                        BluetoothAdapter.STATE_DISCONNECTED
                    )
                    onConnectionStateChanged(device, state, preState)
                }
                BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED -> {
                    onLocalDeviceNameChanged(intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME)?:"")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)?:return
                    val name: String? = intent.getStringExtra(BluetoothDevice.EXTRA_NAME)
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, 0)
                    onDeviceFound(device, name, rssi)
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)
                    val preState =
                        intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE)
                    onBondStateChanged(device, state, preState)
                }
            }
        }
    }

    override fun onStartDiscovery(intent: Intent) {
        "onStartDiscovery".printLine(TAG)
        listener?.onStartDiscovery(intent)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postDiscovery(DiscoveryEntity(DiscoveryState.Start))
        }
    }

    override fun onFinishDiscovery(intent: Intent) {
        "onFinishDiscovery".printLine(TAG)
        listener?.onFinishDiscovery(intent)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postDiscovery(DiscoveryEntity(DiscoveryState.Finish))
        }
    }

    override fun onDeviceFound(device: BluetoothDevice, name: String?, rssi: Short) {
        if (strategy.removeDeviceOfEmptyName()) {
            if (!TextUtils.isEmpty(device.name) || (name != null && name.isNotEmpty())) {
                "onDeviceFound (%s,%s,%s,%s,%s) name=%s rssi=%s".formatLog(
                    TAG,
                    device.name, device.address,
                    device.type.deviceType(), device.bondState.bondState(),
                    device.bluetoothClass?.majorDeviceClass?.majorClass(),
                    name, rssi
                )
                listener?.onDeviceFound(device, name, rssi)
                if (strategy.enableLiveData()) {
                    EasyPrinter.get().postDiscovery(DiscoveryEntity(DiscoveryState.Ing, device, name, rssi))
                }
            }
        } else {
            "onDeviceFound (%s,%s) name=%s rssi=%s".formatLog(TAG, device.name, device.address, name, rssi)
            listener?.onDeviceFound(device, name, rssi)
            if (strategy.enableLiveData()) {
                EasyPrinter.get().postDiscovery(DiscoveryEntity(DiscoveryState.Ing, device, name, rssi))
            }
        }
    }

    override fun onLocalStateChanged(state: Int, previousState: Int) {
        "onLocalStateChanged current=%s previous=%s".formatLog(TAG, state.localState(), previousState.localState())
        listener?.onLocalStateChanged(state, previousState)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postLocalState(LocalStateEntity(state, previousState))
        }
    }

    override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int, previousState: Int) {
        "onConnectionStateChanged (%s,%s),current=%s previous=%s".formatLog(
            TAG,
            device?.name,
            device?.address,
            state.connectionState(),
            previousState.connectionState()
        )
        listener?.onConnectionStateChanged(device, state, previousState)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postConnection(ConnectionEntity(device, state, previousState))
        }
    }

    override fun onLocalDeviceNameChanged(name: String) {
        "onLocalDeviceNameChanged %s".formatLog(TAG, name)
        listener?.onLocalDeviceNameChanged(name)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postLocalName(name)
        }
    }

    override fun onBondStateChanged(device: BluetoothDevice?, state: Int, previousState: Int) {
        "onBondStateChanged (%s,%s) current=%s previous=%s".formatLog(
            TAG,
            device?.name,
            device?.address,
            state.bondState(),
            previousState.bondState()
        )
        listener?.onBondStateChanged(device, state, previousState)
        if (strategy.enableLiveData()) {
            EasyPrinter.get().postBond(BondEntity(device, state, previousState))
        }
    }
}