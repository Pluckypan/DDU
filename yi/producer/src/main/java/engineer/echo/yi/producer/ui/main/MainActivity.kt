package engineer.echo.yi.producer.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import engineer.echo.yi.producer.R
import engineer.echo.yi.producer.databinding.ProducerActivityMainBinding

class MainActivity : AppCompatActivity(), MainContract.IView {

    private lateinit var binding: ProducerActivityMainBinding
    private lateinit var viewModel: MainContract.IViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.producer_activity_main)
        binding.apply {
            this.lifecycleOwner = this@MainActivity
            this.iViewModel = viewModel
        }
    }

    companion object {
        fun goto(context: Context) {
            Intent(context, MainActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}
