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
import engineer.echo.yi.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.*
import okhttp3.Request
import okhttp3.Response

class CoroutinesActivity : AppCompatActivity(), CoroutinesContract.IView {

    private lateinit var viewModel: CoroutinesContract.IViewModel
    private lateinit var binding: ActivityCoroutinesBinding
    private val model = CoroutinesModel()

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

    private val uiScope = CoroutineScope(Dispatchers.Main)

    @WorkerThread
    fun job(): Response? {
        return EasyApi.getClient()?.newCall(Request.Builder().url(IpLocateApi.API_URL).build())
            ?.execute()
    }

    override fun runJob(view: View) {
        uiScope.launch {
            var text = "loading thread=${Thread.currentThread().name}"
            binding.outputTv.text = text
            val res1 = withContext(Dispatchers.IO) {
                job()
            }
            val res2 = async(Dispatchers.IO){
                job()
            }
            text =
                "res1=${res1?.code()} res2=${res2.await()?.code()} thread=${Thread.currentThread().name}"
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
