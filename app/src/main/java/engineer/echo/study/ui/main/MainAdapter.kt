package engineer.echo.study.ui.main

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.AutoFlowLayout
import com.example.library.FlowAdapter
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.cmpts.getColor
import engineer.echo.study.cmpts.toPx

class MainAdapter(callback: MainAdapterCallback? = null) : RecyclerView.Adapter<MainAdapter.SubjectHolder>() {

    private var mCallback: MainAdapterCallback? = callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_subject_main_app, parent, false).also {
            return SubjectHolder(it)
        }
    }

    override fun getItemCount(): Int {
        return C.SUBJECTS.size
    }

    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        val item = C.SUBJECTS[position]
        val aColor = holder.itemView.getColor(item.color)
        holder.tvTitle.setText(item.title)
        holder.tvTitle.setTextColor(aColor)
        holder.tvIcon.setTextColor(aColor)
        holder.tvIcon.setText(item.icon)
        holder.tvIcon.typeface = C.ICON.value
        val subItem = item.subItem
        holder.autoFlow.setAdapter(object : FlowAdapter<Int>(subItem) {
            override fun getView(index: Int): View {
                val itemView = View.inflate(holder.itemView.context, R.layout.item_subject_flow_app, null)
                itemView.findViewById<TextView>(R.id.tv_subject_item_app).apply {
                    setText(subItem[index])
                    setTextColor(aColor)
                    val bg = background.mutate()
                    if (bg is GradientDrawable) {
                        bg.setStroke(1f.toPx(), aColor)
                    }
                }
                return itemView
            }
        })
        holder.autoFlow.setOnItemClickListener { i, _ ->
            mCallback?.onItemClick(i, subItem[i])
        }
    }

    class SubjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIcon = itemView.findViewById<TextView>(R.id.icon_bezier_group_item_app)!!
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_bezier_group_item_app)!!
        val autoFlow = itemView.findViewById<AutoFlowLayout<Int>>(R.id.flow_bezier_item_app)!!
    }

    interface MainAdapterCallback {
        fun onItemClick(pos: Int, subItemId: Int)
    }
}