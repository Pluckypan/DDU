package engineer.echo.yi.consumer.ui.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import engineer.echo.yi.consumer.R
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

    override fun onActivityClose(view: View) {
        onBackPressed()
    }

    companion object {

        internal const val KEY_UID = "uid"

        fun goto(activity: Activity, uid: String) {
            Intent(activity, FriendsActivity::class.java).also {
                it.putExtra(KEY_UID, uid)
                activity.startActivity(it)
            }
        }
    }
}
