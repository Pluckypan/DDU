package engineer.echo.easyprinter

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 *  Monitor.kt
 *  Info: 蓝牙状态接收
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 10:05 AM
 *  more about me: http://www.1991th.com
 */
class Monitor(private val listener: IMonitor? = null) : BroadcastReceiver(), IMonitor {

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
                    onLocalDeviceNameChanged(intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME))
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME)
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
        listener?.onStartDiscovery(intent)
    }

    override fun onFinishDiscovery(intent: Intent) {
        listener?.onFinishDiscovery(intent)
    }

    override fun onLocalStateChanged(state: Int, previousState: Int) {
        listener?.onLocalStateChanged(state, previousState)
    }

    override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int, previousState: Int) {
        listener?.onConnectionStateChanged(device, state, previousState)
    }

    override fun onLocalDeviceNameChanged(name: String) {
        listener?.onLocalDeviceNameChanged(name)
    }

    override fun onDeviceFound(device: BluetoothDevice, name: String, rssi: Short) {
        listener?.onDeviceFound(device, name, rssi)
    }

    override fun onBondStateChanged(device: BluetoothDevice?, state: Int, previousState: Int) {
        listener?.onBondStateChanged(device, state, previousState)
    }
}