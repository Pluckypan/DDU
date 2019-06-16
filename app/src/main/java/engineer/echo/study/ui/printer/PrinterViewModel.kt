package engineer.echo.study.ui.printer

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel

class PrinterViewModel : ViewModel(), PrinterContract.IVModel {

    init {

    }

    private val mDeviceList = arrayListOf<BluetoothDevice>()

    override fun onCleared() {
        super.onCleared()
    }
}