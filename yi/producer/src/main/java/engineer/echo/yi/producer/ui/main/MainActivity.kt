package engineer.echo.yi.producer.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.yi.producer.R
import engineer.echo.yi.producer.databinding.ProducerActivityMainBinding

class MainActivity : AppCompatActivity(), MainContract.IView {

    private lateinit var binding: ProducerActivityMainBinding
    private lateinit var viewModel: MainContract.IViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.producer_activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    companion object {
        fun goto(context: Context) {
            Intent(context, MainActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}
