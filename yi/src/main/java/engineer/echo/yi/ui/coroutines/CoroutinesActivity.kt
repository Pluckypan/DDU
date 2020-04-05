package engineer.echo.yi.ui.coroutines

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.R
import engineer.echo.yi.api.IpLocateApi
import engineer.echo.yi.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response

class CoroutinesActivity : AppCompatActivity(), CoroutinesContract.IView {

    private lateinit var viewModel: CoroutinesContract.IViewModel
    private lateinit var binding: ActivityCoroutinesBinding
    private val model = CoroutinesModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            CoroutinesViewModel.Factory(intent.extras, application)
        ).get(CoroutinesViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_coroutines)

        binding.apply {
            lifecycleOwner = this@CoroutinesActivity
            view = this@CoroutinesActivity
            viewModel = this@CoroutinesActivity.viewModel
        }
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun runJob(view: View) {
        uiScope.launch {
            var text = "loading thread=${Thread.currentThread().name}"
            binding.outputTv.text = text
            var location: Response? = null
            withContext(Dispatchers.IO) {
                location =
                    EasyApi.getClient()?.newCall(Request.Builder().url(IpLocateApi.API_URL).build())
                        ?.execute()
            }
            text = "${location?.code()} thread=${Thread.currentThread().name}"
            binding.outputTv.text = text
        }
    }

    companion object {

        @BindingAdapter("easyJobData")
        @JvmStatic
        fun onBindJob(textView: TextView, job: Long?) {
            textView.text = if (job != null) "EasyJob $job" else "Easy MVVM"
        }

        fun goto(activity: Activity) {
            Intent(activity, CoroutinesActivity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
