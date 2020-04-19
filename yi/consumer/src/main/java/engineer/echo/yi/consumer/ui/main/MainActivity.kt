package engineer.echo.yi.consumer.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import engineer.echo.yi.common.EasyApp
import engineer.echo.yi.common.cpmts.glide.EasyPicture
import engineer.echo.yi.consumer.R
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.cmpts.weibo.WeiboAuthHandler
import engineer.echo.yi.consumer.cmpts.weibo.bean.Account
import engineer.echo.yi.consumer.databinding.ConsumerMainBinding
import engineer.echo.yi.consumer.ui.friends.FriendsActivity

class MainActivity : AppCompatActivity(), MainContract.IView {

    private lateinit var binding: ConsumerMainBinding
    private lateinit var viewModel: MainContract.IViewModel
    private var authHandler: WeiboAuthHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

    override fun onFriendsList(view: View) {
        viewModel.accountInfoData.value?.id?.let {
            FriendsActivity.goto(this, it)
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

        @BindingAdapter("accountInfoData")
        @JvmStatic
        fun bindUserInfo(textView: TextView, account: Account?) {
            textView.text = account?.screenName?.plus("\n")?.plus(account.description) ?: "Plucky"
        }

        @BindingAdapter("accountInfoData")
        @JvmStatic
        fun bindUserInfo(imageView: ImageView, account: Account?) {
            EasyPicture.with(imageView)
                .load(account?.avatar ?: "")
                .placeholder(ColorDrawable(EasyApp.getColor(R.color.common_colorPrimaryDark)))
                .circleCrop()
                .into(imageView)
        }

        @BindingAdapter("onFriendsCount")
        @JvmStatic
        fun bindFriendsCount(button: Button, account: Account?) {
            button.isEnabled = account != null && account.friendsCount > 0
            button.text =
                EasyApp.getString(R.string.consumer_fmt_friends, account?.friendsCount ?: 0)
        }
    }
}
