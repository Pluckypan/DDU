package engineer.echo.yi.ui.coroutines

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.R
import engineer.echo.yi.api.IpLocateApi
import engineer.echo.yi.common.cpmts.currentThreadName
import engineer.echo.yi.common.cpmts.uiScope
import engineer.echo.yi.databinding.ActivityCoroutinesBinding
import engineer.echo.yi.ui.mock.ApiMockActivity
import kotlinx.coroutines.*
import okhttp3.Request
import okhttp3.Response

class CoroutinesActivity : AppCompatActivity(), CoroutinesContract.IView {

    private lateinit var viewModel: CoroutinesContract.IViewModel
    private lateinit var binding: ActivityCoroutinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
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


    @WorkerThread
    fun job(): Response? {
        return EasyApi.getClient()?.newCall(Request.Builder().url(IpLocateApi.API_URL).build())
            ?.execute()
    }

    override fun runJob(view: View) {
        lifecycle.uiScope.launch {
            var text = "loading thread=$currentThreadName"
            binding.outputTv.text = text
            val res1 = withContext(Dispatchers.IO) {
                job()
            }
            val res2 = async(Dispatchers.IO) {
                job()
            }
            val c1 = res1?.code()
            val c2 = res2.await()?.code()
            text = "c1=$c1 c2=$c2 thread=${currentThreadName}"
            binding.outputTv.text = text
            ApiMockActivity.goHome(this@CoroutinesActivity)
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
