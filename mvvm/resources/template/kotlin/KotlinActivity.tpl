package $currentPackage$

import android.app.Activity
import android.content.Intent
import $baseActivityPackage$
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import $rootPackage$.R
import $rootPackage$.databinding.Activity$moduleName$Binding

class $moduleName$Activity : $baseActivity$(), $moduleName$Contract.IView {

    private lateinit var viewModel: $moduleName$Contract.IViewModel
    private lateinit var binding: Activity$moduleName$Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            $moduleName$ViewModel.Factory(intent.extras, application)
        ).get($moduleName$ViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_$activityAffix$)

        binding.apply {
            lifecycleOwner = this@$moduleName$Activity
            view = this@$moduleName$Activity
            viewModel = this@$moduleName$Activity.viewModel
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
            Intent(activity, $moduleName$Activity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
