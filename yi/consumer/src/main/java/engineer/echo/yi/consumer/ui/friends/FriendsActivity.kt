package engineer.echo.yi.consumer.ui.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.yi.consumer.R
import engineer.echo.yi.consumer.cmpts.weibo.bean.UserList
import engineer.echo.yi.consumer.databinding.ConsumerActivityFriendsBinding

class FriendsActivity : AppCompatActivity(), FriendsContract.IView {

    private lateinit var viewModel: FriendsContract.IViewModel
    private lateinit var binding: ConsumerActivityFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            FriendsViewModel.Factory(intent.extras, application)
        ).get(FriendsViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.consumer_activity_friends)

        binding.apply {
            lifecycleOwner = this@FriendsActivity
            view = this@FriendsActivity
            viewModel = this@FriendsActivity.viewModel
        }
    }

    override fun toast(view: View) {
        Toast.makeText(this, "Hello EasyMVVM.${view.javaClass.simpleName}", Toast.LENGTH_LONG)
            .show()
    }

    companion object {

        internal const val KEY_UID = "uid"

        @BindingAdapter("userListData")
        @JvmStatic
        fun onBindJob(textView: TextView, userList: UserList?) {
            textView.text = "${userList?.totalNumber ?: 0}"
        }

        fun goto(activity: Activity, uid: String) {
            Intent(activity, FriendsActivity::class.java).also {
                it.putExtra(KEY_UID, uid)
                activity.startActivity(it)
            }
        }
    }
}
