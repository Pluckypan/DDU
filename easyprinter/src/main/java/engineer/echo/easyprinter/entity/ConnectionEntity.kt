package engineer.echo.easyprinter.entity

import android.bluetooth.BluetoothDevice

/**
 *  ConnectionEntity.kt
 *  Info: ConnectionEntity.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 3:10 PM
 *  more about me: http://www.1991th.com
 */
data class ConnectionEntity(
    val device: BluetoothDevice? = null,
    val state: Int,
    val previousState: Int
)