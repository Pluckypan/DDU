package engineer.echo.yi.ui.mock

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper
import engineer.echo.yi.R
import engineer.echo.yi.bean.weather.WeatherResp
import engineer.echo.yi.bean.weather.WeatherResult
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.databinding.WeatherOtherBinding
import engineer.echo.yi.databinding.WeatherTodayBinding

object WeatherRecyclerViewHelper {


    private fun RecyclerView.setupIfNeed(viewModel: ApiMockContract.IViewModel? = null): WeatherAdapter {
        if (adapter == null) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherAdapter()
            GravityPagerSnapHelper(Gravity.START, true) {
                viewModel?.indicatorData?.value?.let { pair ->
                    if (it != pair.first) {
                        viewModel.indicatorData.postValue(Pair(it, pair.second))
                    }
                }
            }.attachToRecyclerView(this)
        }
        return adapter as WeatherAdapter
    }

    private fun RecyclerView.ViewHolder.setupItemSize() {
        EasyApp.displayMetrics.widthPixels.let {
            itemView.layoutParams = ViewGroup.LayoutParams(it, it)
        }
    }

    private const val TYPE_TODAY = 1
    private const val TYPE_OTHER = 2

    @JvmStatic
    @BindingAdapter("weatherData", "viewModel")
    fun bindData(
        recyclerView: RecyclerView,
        weather: WeatherResp? = null,
        viewModel: ApiMockContract.IViewModel? = null
    ) {
        recyclerView.setupIfNeed(viewModel).apply {
            data = weather?.results?.firstOrNull()?.also {
                viewModel?.indicatorData?.postValue(Pair(0, it.weather_data.size))
            }
            notifyDataSetChanged()
        }
    }

    private class WeatherAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var data: WeatherResult? = null
        private val inflater: LayoutInflater = LayoutInflater.from(EasyApp.getApp())

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
        // 方式一
        val binding: WeatherTodayBinding? = WeatherTodayBinding.bind(itemView)

        init {
            setupItemSize()
        }
    }

    class WeatherOtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 方式二
        val binding = DataBindingUtil.bind<WeatherOtherBinding>(itemView)

        init {
            setupItemSize()
        }
    }
}