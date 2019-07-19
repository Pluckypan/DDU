package engineer.echo.study.ui.ipc.printer

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.databinding.DeviceItemBinding
import engineer.echo.study.ui.ipc.printer.PrinterFragment.Companion.majorIcon

/**
 *  DeviceListAdapter.kt
 *  Info: DeviceListAdapter.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 12:08 AM
 *  more about me: http://www.1991th.com
 */
class DeviceListAdapter(private val iView: PrinterContract.IView) :
    RecyclerView.Adapter<DeviceListAdapter.DeviceHolder>() {

    companion object {

        @BindingAdapter("deviceIcon")
        @JvmStatic
        fun onDeviceIcon(textView: TextView, device: BluetoothDevice?) {
            textView.setText(device?.majorIcon() ?: R.string.iconBluetooth)
        }

        @BindingAdapter("deviceBond")
        @JvmStatic
        fun onDeviceBond(view: View, device: BluetoothDevice?) {
            view.isSelected = device != null && EasyPrinter.get().isBonded(device)
        }
    }

    private val mData = arrayListOf<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        return DeviceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_printer_app, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.bind(getItem(position), iView)
    }

    fun getItem(position: Int): BluetoothDevice {
        return mData[position]
    }

    fun setList(list: List<BluetoothDevice>) {
        mData.clear()
        if (list.isNotEmpty()) {
            mData.addAll(list)
        }
        notifyDataSetChanged()
    }

    class DeviceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding = DataBindingUtil.bind<DeviceItemBinding?>(itemView).apply {
            this!!.tvIconPrinterApp.typeface = C.ICON.value
        }

        fun bind(device: BluetoothDevice, iView: PrinterContract.IView) {
            mBinding?.device = device
            mBinding?.iView = iView
        }
    }
}