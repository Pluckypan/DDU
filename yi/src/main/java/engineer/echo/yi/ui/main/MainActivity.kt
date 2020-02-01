package engineer.echo.yi.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.yi.R
import engineer.echo.yi.bean.weather.WeatherResp
import engineer.echo.yi.databinding.MainActivityBinding

class MainActivity : AppCompatActivity(), MainContract.IView {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainContract.IViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                iView = this@MainActivity
                iViewModel = viewModel
            }
        viewModel.weatherData?.observe(this, Observer {
            title = it.getWeather(getString(R.string.app_name))
        })
    }

    override fun onDownloadClick(view: View) {
        viewModel.startDownload(this)
    }

    override fun onCancelDownloadClick(view: View) {
        viewModel.cancelDownload()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("weatherData")
        fun onBindWeather(textView: TextView, weather: WeatherResp? = null) {
            weather?.let {
                textView.text = it.getWeather()
            }
        }

        @JvmStatic
        @BindingAdapter("downloadData")
        fun onBindDownload(textView: TextView, downloadState: DownloadState? = null) {
            textView.text = downloadState?.downloadText() ?: ""
        }

        @JvmStatic
        @BindingAdapter("downloadData")
        fun onBindDownload(textView: Button, downloadState: DownloadState? = null) {
            textView.isEnabled = downloadState == null || downloadState.downloadEnable()
        }
    }
}
