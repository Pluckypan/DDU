package engineer.echo.study.ui.costom

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import engineer.echo.study.App
import engineer.echo.study.R

class SizeRevAdapter : RecyclerView.Adapter<SizeRevAdapter.SizeHolder>() {


    private val size = App.getApp().resources.getDimensionPixelSize(R.dimen.size_recyclerview_item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeHolder {
        val view = TextView(parent.context).apply {
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.shape_gray_stroke)
            layoutParams = ViewGroup.LayoutParams(size, size)
        }
        return SizeHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: SizeHolder, position: Int) {
        holder.textView.text = "$position"
    }

    class SizeHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView
    }
}