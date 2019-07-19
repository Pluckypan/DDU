package engineer.echo.study.ui.ipc.p2p

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.whisper.WhisperDevice

class WifiListAdapter() : RecyclerView.Adapter<WifiListAdapter.WifiHolder>() {

    private val mDataSet = arrayListOf<WhisperDevice>()
    private var mListener: WifiListAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_p2p_ipc, parent, false)
        return WifiHolder(view).apply {
            itemView.setOnClickListener {
                mListener?.onItemClick(getItem(adapterPosition))
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private fun getItem(position: Int): WhisperDevice {
        return mDataSet[position]
    }

    fun setListener(listener: WifiListAdapterListener?) {
        this.mListener = listener
    }

    override fun onBindViewHolder(holder: WifiHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            tvIcon.setText(R.string.iconWifi)
            tvIcon.typeface = C.ICON.value
            tvName.text = item.name
            tvAddr.text = item.address
        }
    }

    fun setData(list: List<WhisperDevice>?) {
        mDataSet.clear()
        if (list?.isNotEmpty() == true) {
            mDataSet.addAll(list)
        }
        notifyDataSetChanged()
    }

    class WifiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIcon = itemView.findViewById<TextView>(R.id.tv_icon_ipc)!!
        val tvName = itemView.findViewById<TextView>(R.id.tv_name_ipc)!!
        val tvAddr = itemView.findViewById<TextView>(R.id.tv_address_ipc)!!
    }

    interface WifiListAdapterListener {
        fun onItemClick(device: WhisperDevice)
    }
}