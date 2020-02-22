package engineer.echo.yi.im.ui.kotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.yi.im.R
import engineer.echo.yi.im.databinding.ActivityKotlinBinding

class KotlinActivity : AppCompatActivity(), KotlinContract.IView {

    private lateinit var viewModel: KotlinContract.IViewModel
    private lateinit var binding: ActivityKotlinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            KotlinViewModel.Factory(savedInstanceState, application)
        ).get(KotlinViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kotlin)

        binding.apply {
            lifecycleOwner = this@KotlinActivity
            view = this@KotlinActivity
            viewModel = this@KotlinActivity.viewModel
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
            Intent(activity, KotlinActivity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
