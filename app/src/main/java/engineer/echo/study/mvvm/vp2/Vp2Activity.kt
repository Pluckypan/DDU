package engineer.echo.study.mvvm.vp2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import engineer.echo.study.R
import engineer.echo.study.databinding.ActivityVp2Binding

class Vp2Activity : AppCompatActivity(), Vp2Contract.IView {

    private lateinit var viewModel: Vp2Contract.IViewModel
    private lateinit var binding: ActivityVp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            Vp2ViewModel.Factory(intent.extras, application)
        ).get(Vp2ViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vp2)

        binding.apply {
            lifecycleOwner = this@Vp2Activity
            view = this@Vp2Activity
            viewModel = this@Vp2Activity.viewModel
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
            Intent(activity, Vp2Activity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
