package engineer.echo.study.ui.printer

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

object PrinterContract {

    interface IVModel {
        fun onCreate(owner: LifecycleOwner)
        fun observeDeviceList(owner: LifecycleOwner, observer: Observer<List<BluetoothDevice>>)
    }

    interface IView {
        fun onDeviceItemClick(device: BluetoothDevice? = null)
    }

    interface IModel {

    }
}