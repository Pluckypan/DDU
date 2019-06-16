package engineer.echo.easyprinter.entity

import android.bluetooth.BluetoothDevice

/**
 *  BondEntity.kt
 *  Info: BondEntity.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 3:33 PM
 *  more about me: http://www.1991th.com
 */
data class BondEntity(
    val device: BluetoothDevice? = null,
    val state: Int,
    val previousState: Int
)