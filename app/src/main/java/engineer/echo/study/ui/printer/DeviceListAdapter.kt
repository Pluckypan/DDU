package engineer.echo.study.ui.printer

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import engineer.echo.study.R
import engineer.echo.study.databinding.DeviceItemBinding

/**
 *  DeviceListAdapter.kt
 *  Info: DeviceListAdapter.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/16 - 12:08 AM
 *  more about me: http://www.1991th.com
 */
class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.DeviceHolder>() {

    private val mData = arrayListOf<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        return DeviceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_printer_app, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItem(position: Int): BluetoothDevice {
        return mData[position]
    }

    class DeviceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding = DataBindingUtil.bind<DeviceItemBinding?>(itemView)

        fun bind(device: BluetoothDevice) {
            mBinding?.device = device
        }
    }
}