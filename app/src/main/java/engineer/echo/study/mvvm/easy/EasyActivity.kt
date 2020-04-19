package engineer.echo.study.mvvm.easy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import engineer.echo.study.R
import engineer.echo.study.databinding.ActivityEasyBinding

class EasyActivity : AppCompatActivity(), EasyContract.IView {

    private lateinit var viewModel: EasyContract.IViewModel
    private lateinit var binding: ActivityEasyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            EasyViewModel.Factory(intent.extras, application)
        ).get(EasyViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_easy)

        binding.apply {
            lifecycleOwner = this@EasyActivity
            view = this@EasyActivity
            viewModel = this@EasyActivity.viewModel
        }
    }

    override fun toast(view: View) {
        Toast.makeText(this, "Hello EasyMVVM.${view.javaClass.simpleName}", Toast.LENGTH_LONG)
            .show()
    }

    companion object {

        @BindingAdapter("easyJobData")
        @JvmStatic
        fun onBindJob(textView: TextView, job: Long?) {
            textView.text = if (job != null) "EasyJob $job" else "Easy MVVM"
        }

        fun goto(activity: Activity) {
            Intent(activity, EasyActivity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
