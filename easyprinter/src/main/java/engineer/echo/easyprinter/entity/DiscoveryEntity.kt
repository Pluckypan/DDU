package engineer.echo.easyprinter.entity

import android.bluetooth.BluetoothDevice

/**
 *  DiscoveryEntity.kt
 *  Info: DiscoveryEntity.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 1:13 PM
 *  more about me: http://www.1991th.com
 */
data class DiscoveryEntity(
    var state: DiscoveryState,
    var device: BluetoothDevice? = null,
    var name: String? = null,
    var rssi: Short = 0
) {
}