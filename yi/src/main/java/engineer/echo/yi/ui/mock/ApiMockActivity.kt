package engineer.echo.yi.ui.mock

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import engineer.echo.easyapi.EasyApi
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.download.DownloadState
import engineer.echo.easylib.memInfo
import engineer.echo.yi.R
import engineer.echo.yi.bean.location.IpLocation
import engineer.echo.yi.cmpts.widgets.IndicatorView
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.common.Proxy
import engineer.echo.yi.common.cpmts.glide.EasyPicture
import engineer.echo.yi.databinding.MainActivityBinding
import engineer.echo.yi.ui.coroutines.CoroutinesActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.*

class ApiMockActivity : AppCompatActivity(), ApiMockContract.IView {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: ApiMockContract.IViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ApiMockViewModel.Factory(memInfo()))
            .get(ApiMockViewModel::class.java)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.activity_mock)
            .apply {
                lifecycleOwner = this@ApiMockActivity
                iView = this@ApiMockActivity
                iViewModel = viewModel
            }
        setupView()
    }

    override fun onResume() {
        super.onResume()
        binding.mockWindLottieView.playAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.mockWindLottieView.pauseAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.proxy, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_consumer -> EasyApi.getProxy(Proxy.ConsumerApi::class.java).goto(this)
            R.id.menu_im -> EasyApi.getProxy(Proxy.IMApi::class.java).goto(this)
            R.id.menu_live -> EasyApi.getProxy(Proxy.LiveApi::class.java).goto(this)
            R.id.menu_producer -> EasyApi.getProxy(Proxy.ProducerApi::class.java).goto(this)
            R.id.menu_coroutines -> CoroutinesActivity.goto(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDownloadClick(view: View) {
        viewModel.startDownload()
    }

    override fun onCancelDownloadClick(view: View) {
        viewModel.cancelDownload()
    }

    override fun onZipClick(view: View) {
        viewModel.startZipAction()
    }

    private fun setupView() {
        // title
        viewModel.titleData.observe(this, Observer {
            title = it
        })

        // refreshLayout
        binding.mockRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.refresh()
            }
            autoRefresh()
        }

        viewModel.weatherData.observe(this, Observer {
            binding.mockRefreshLayout.finishRefresh(500, it.isWeatherSuccess())
        })

        // background
        EasyPicture.with(this)
            .load(getBackground())
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(ColorDrawable(EasyApp.getColor(R.color.common_colorPrimaryDark)))
            .transform(BG_TRANSFORM)
            .into(binding.mockBgIv)
    }

    companion object {

        private const val BG_DAY = "http://img.1991th.com/tuchongeter/statics/HSDH3OCE5QQKWYR"
        private const val BG_NIGHT = "http://img.1991th.com/tuchongeter/statics/XO5DIZ7YXGU5EVN"
        private val BG_TRANSFORM = MultiTransformation(
            BlurTransformation(10, 2),
            CenterCrop()
        )

        private fun getBackground(): String {
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY).let {
                return if (it in 6..18) BG_DAY else BG_NIGHT
            }
        }

        @JvmStatic
        @BindingAdapter("indicatorData")
        fun onBindIndicator(indicatorView: IndicatorView, pair: Pair<Int, Int>? = null) {
            pair?.let {
                indicatorView.size = pair.second
                indicatorView.currentIndex = pair.first
            }
        }

        @JvmStatic
        @BindingAdapter("downloadData", "zipData", "switchData")
        fun onBindDownload(
            textView: TextView,
            downloadState: DownloadState? = null,
            zipData: Result? = null,
            switchData: Boolean? = null
        ) {
            val downloadText = downloadState?.downloadText() ?: ""
            val zipText = zipData?.let {
                "${EasyApp.getString(
                    if (switchData == true) R.string.common_label_zip else R.string.common_label_unzip
                )} ${zipData.isSuccess()}"
            } ?: ""
            textView.text = zipText.plus(" ").plus(downloadText)
        }

        @JvmStatic
        @BindingAdapter("locationData")
        fun onBindLocation(textView: TextView, location: IpLocation? = null) {
            textView.text = location?.getLocation(EasyApp.getString(R.string.tips_network_error))
        }

        @JvmStatic
        @BindingAdapter("downloadData")
        fun onBindDownload(textView: Button, downloadState: DownloadState? = null) {
            textView.isEnabled = downloadState == null || downloadState.downloadEnable()
        }

        @JvmStatic
        @BindingAdapter("switchData")
        fun onBindZip(textView: Button, switchData: Boolean? = null) {
            textView.setText(if (switchData == true) R.string.common_label_zip else R.string.common_label_unzip)
        }
    }
}
