package engineer.echo.yi.ui.mock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import engineer.echo.yi.R
import engineer.echo.yi.YiApp
import engineer.echo.yi.bean.weather.WeatherResp
import engineer.echo.yi.bean.weather.WeatherResult
import engineer.echo.yi.databinding.WeatherOtherBinding
import engineer.echo.yi.databinding.WeatherTodayBinding

object WeatherRecyclerViewHelper {


    private fun RecyclerView.setupIfNeed(): WeatherAdapter {
        if (adapter == null) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherAdapter()
        }
        return adapter as WeatherAdapter
    }

    private fun RecyclerView.ViewHolder.setupItemSize() {
        YiApp.displayMetrics.widthPixels.let {
            itemView.layoutParams = ViewGroup.LayoutParams(it, it)
        }
    }

    private const val TYPE_TODAY = 1
    private const val TYPE_OTHER = 2

    @JvmStatic
    @BindingAdapter("weatherData")
    fun bindData(recyclerView: RecyclerView, weather: WeatherResp? = null) {
        recyclerView.setupIfNeed().apply {
            data = weather?.results?.firstOrNull()
            notifyDataSetChanged()
        }
    }

    private class WeatherAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var data: WeatherResult? = null
        private val inflater: LayoutInflater = LayoutInflater.from(YiApp.getApp())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == TYPE_TODAY)
                WeatherTodayViewHolder(inflater.inflate(R.layout.item_weather_today, parent, false))
            else
                WeatherOtherViewHolder(inflater.inflate(R.layout.item_weather_other, parent, false))

        }

        override fun getItemCount(): Int {
            return data?.weather_data?.size ?: 0
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) TYPE_TODAY else TYPE_OTHER
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is WeatherTodayViewHolder -> {
                    holder.binding?.weather = data!!
                }
                is WeatherOtherViewHolder -> {
                    holder.binding?.weather = data!!.weather_data[position]
                }
            }
        }
    }

    class WeatherTodayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: WeatherTodayBinding? = WeatherTodayBinding.bind(itemView)

        init {
            setupItemSize()
        }
    }

    class WeatherOtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<WeatherOtherBinding>(itemView)

        init {
            setupItemSize()
        }
    }
}