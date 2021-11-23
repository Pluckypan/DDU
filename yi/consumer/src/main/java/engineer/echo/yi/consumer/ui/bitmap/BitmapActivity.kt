package engineer.echo.yi.consumer.ui.bitmap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import engineer.echo.yi.consumer.R
import engineer.echo.yi.consumer.databinding.ConsumerActivityBitmapBinding

class BitmapActivity : AppCompatActivity(), BitmapContract.IView {

    private lateinit var viewModel: BitmapContract.IViewModel
    private lateinit var binding: ConsumerActivityBitmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            BitmapViewModel.Factory(intent.extras, application)
        ).get(BitmapViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.consumer_activity_bitmap)
        binding.apply {
            van.post {
                cbL.isChecked = van.poster.neighborDistribution[0]
                cbT.isChecked = van.poster.neighborDistribution[1]
                cbR.isChecked = van.poster.neighborDistribution[2]
                cbB.isChecked = van.poster.neighborDistribution[3]
            }
        }

        binding.apply {
            lifecycleOwner = this@BitmapActivity
            view = this@BitmapActivity
            viewModel = this@BitmapActivity.viewModel
        }
    }

    override fun toast(view: View) {
        Toast.makeText(this, "Hello EasyMVVM.${view.javaClass.simpleName}", Toast.LENGTH_LONG)
            .show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.id == R.id.cbV) {
            binding.van.vertical.set(isChecked)
            binding.van.invalidate()
            return
        }
        if (buttonView.id == R.id.cbType) {
            binding.van.type.set(isChecked)
            binding.van.invalidate()
            return
        }
        val index = when (buttonView.id) {
            R.id.cbL -> 0
            R.id.cbT -> 1
            R.id.cbR -> 2
            R.id.cbB -> 3
            else -> 0
        }
        binding.van.update(index, isChecked)
    }

    companion object {

        @BindingAdapter("easyJobData")
        @JvmStatic
        fun onBindJob(textView: TextView, job: Long?) {
            textView.text = if (job != null) "EasyJob $job" else "Easy MVVM"
        }

        fun goto(activity: Activity) {
            Intent(activity, BitmapActivity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
