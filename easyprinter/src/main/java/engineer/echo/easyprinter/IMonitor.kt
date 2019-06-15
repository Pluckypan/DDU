package engineer.echo.easyprinter

import android.bluetooth.BluetoothDevice
import android.content.Intent

/**
 *  IMonitor.kt
 *  Info: 蓝牙状态接口
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/14 - 10:06 AM
 *  more about me: http://www.1991th.com
 */
interface IMonitor {
    /**
     * Register for {@link BluetoothDevice#ACTION_FOUND} to be notified as remote Bluetooth devices are found.
     * BluetoothAdapter.ACTION_DISCOVERY_STARTED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onStartDiscovery(intent: Intent)

    /**
     * The local Bluetooth adapter has finished the device discovery process.
     * BluetoothAdapter.ACTION_DISCOVERY_FINISHED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onFinishDiscovery(intent: Intent)

    /**
     * @param state  当前状态 {@link BluetoothAdapter#EXTRA_STATE}
     * @param previousState 上一次状态 {@link BluetoothAdapter#EXTRA_PREVIOUS_STATE}
     * int extra field in：
     * [BluetoothAdapter#STATE_OFF]
     * [BluetoothAdapter#STATE_TURNING_ON]
     * [BluetoothAdapter#STATE_ON]
     * [BluetoothAdapter#STATE_TURNING_OFF]
     *
     * BluetoothAdapter.ACTION_STATE_CHANGED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onLocalStateChanged(state: Int, previousState: Int)

    /**
     * @param device {@link BluetoothDevice#EXTRA_DEVICE} - The remote device Used as a Parcelable .
     * @param state 当前连接状态 {@link BluetoothAdapter#EXTRA_CONNECTION_STATE} - The current connection state.
     * @param previousState 上一次连接状态 {@link BluetoothAdapter#EXTRA_PREVIOUS_CONNECTION_STATE}- The previous connection state
     * can be any of:
     * {@link BluetoothAdapter#STATE_DISCONNECTED}
     * {@link BluetoothAdapter#STATE_CONNECTING}
     * {@link BluetoothAdapter#STATE_CONNECTED}
     * {@link BluetoothAdapter#STATE_DISCONNECTING}
     *
     * BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onConnectionStateChanged(device: BluetoothDevice? = null, state: Int, previousState: Int)

    /**
     * 本机设备名称改变
     * @param name Used as a String extra field {@link BluetoothAdapter#EXTRA_LOCAL_NAME}
     *
     * BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onLocalDeviceNameChanged(name: String)

    /**
     * @param device {@link BluetoothDevice#EXTRA_DEVICE} Used as a Parcelable
     * @param name {@link BluetoothDevice#EXTRA_NAME} Used as a String extra field
     * @param rssi {@link BluetoothDevice#EXTRA_RSSI} Used as an optional short extra field
     *
     * BluetoothDevice.ACTION_FOUND
     * Requires {@link android.Manifest.permission#BLUETOOTH} and
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION} to receive.
     */
    fun onDeviceFound(device: BluetoothDevice, name: String? = null, rssi: Short)

    /**
     * 蓝牙绑定状态
     * @param device {@link BluetoothDevice#EXTRA_DEVICE}
     * @param state {@link BluetoothDevice#EXTRA_BOND_STATE}
     * @param previousState {@link BluetoothDevice#EXTRA_PREVIOUS_BOND_STATE}
     * Possible values are:
     * {@link BluetoothDevice#BOND_NONE},
     * {@link BluetoothDevice#BOND_BONDING},
     * {@link BluetoothDevice#BOND_BONDED}.
     *
     * BluetoothDevice.ACTION_BOND_STATE_CHANGED
     * Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    fun onBondStateChanged(device: BluetoothDevice? = null, state: Int, previousState: Int)
}