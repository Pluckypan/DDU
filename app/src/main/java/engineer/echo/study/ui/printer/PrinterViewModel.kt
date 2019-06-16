package engineer.echo.study.ui.printer

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.easyprinter.entity.DiscoveryState

class PrinterViewModel : ViewModel(), PrinterContract.IVModel {

    companion object {
        fun List<BluetoothDevice>.hasDevice(device: BluetoothDevice): Boolean {
            return firstOrNull {
                it.address == device.address && it.name == device.name
            } != null
        }
    }

    private val mDeviceListLiveData = MutableLiveData<List<BluetoothDevice>>()
    private val mDeviceList = arrayListOf<BluetoothDevice>()

    init {

    }

    override fun onCreate(owner: LifecycleOwner) {
        EasyPrinter.get().observeDiscovery(owner, Observer {
            when (it.state) {
                DiscoveryState.Start -> {
                    mDeviceList.clear()
                    mDeviceListLiveData.value = mDeviceList
                }
                DiscoveryState.Ing -> {
                    it.device?.apply {
                        if (!mDeviceList.hasDevice(this)) {
                            mDeviceList.add(this)
                            mDeviceListLiveData.value = mDeviceList
                        } else {

                        }
                    }
                }
                DiscoveryState.Finish -> {
                    mDeviceListLiveData.value = mDeviceList
                }
            }
        })
    }

    override fun observeDeviceList(owner: LifecycleOwner, observer: Observer<List<BluetoothDevice>>) {
        mDeviceListLiveData.observe(owner, observer)
    }

    override fun onCleared() {
        super.onCleared()
    }
}