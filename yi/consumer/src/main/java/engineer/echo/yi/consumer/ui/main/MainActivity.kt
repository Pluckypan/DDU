package engineer.echo.yi.consumer.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.yi.consumer.R
import engineer.echo.yi.consumer.cmpts.weibo.User
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.cmpts.weibo.WeiboAuthHandler
import engineer.echo.yi.consumer.databinding.ConsumerMainBinding

class MainActivity : AppCompatActivity(), MainContract.IView {

    private lateinit var binding: ConsumerMainBinding
    private lateinit var viewModel: MainContract.IViewModel
    private var authHandler: WeiboAuthHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView<ConsumerMainBinding>(
            this,
            R.layout.consumer_activity_main
        ).apply {
            lifecycleOwner = this@MainActivity
            iView = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }
    }

    override fun onAuthorize() {
        authHandler = Weibo.authorize(this)
    }

    override fun onClearAuth() {
        Weibo.clearToken()
        viewModel.triggerToken()
    }

    override fun onRefreshAuth() {
        Weibo.refreshToken { _, _, _ ->
            viewModel.triggerToken()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authHandler?.authorizeCallBack(requestCode, resultCode, data)
        viewModel.triggerToken()
    }

    companion object {
        fun goto(context: Context) {
            Intent(context, MainActivity::class.java).also {
                context.startActivity(it)
            }
        }

        @BindingAdapter("userInfoData")
        @JvmStatic
        fun bindUserInfo(textView: TextView, user: User?) {
            textView.text = user?.toString() ?: "no authorize"
        }
    }
}
