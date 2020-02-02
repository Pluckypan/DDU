package engineer.echo.yi.ui.mock

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
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.bean.weather.WeatherResp
import engineer.echo.yi.databinding.MainActivityBinding

class ApiMockActivity : AppCompatActivity(), ApiMockContract.IView {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: ApiMockContract.IViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ApiMockViewModel::class.java)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.activity_mock)
            .apply {
                lifecycleOwner = this@ApiMockActivity
                iView = this@ApiMockActivity
                iViewModel = viewModel
            }
        viewModel.titleData.observe(this, Observer {
            title = it
        })
    }

    override fun onDownloadClick(view: View) {
        viewModel.startDownload(binding.mockApkSwitch.isChecked)
    }

    override fun onCancelDownloadClick(view: View) {
        viewModel.cancelDownload()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("weatherData", "locationData")
        fun onBindWeather(
            textView: TextView,
            weather: WeatherResp? = null,
            location: IpLocation? = null
        ) {
            location?.let {
                textView.text = it.getLocation().plus("\n\n")
            }
            weather?.let {
                val oldText = textView.text.toString()
                textView.text = oldText.plus(it.getWeather())
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
