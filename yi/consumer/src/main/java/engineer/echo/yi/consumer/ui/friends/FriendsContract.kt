package engineer.echo.yi.consumer.ui.friends

import android.view.View
import androidx.lifecycle.LiveData
import engineer.echo.yi.consumer.cmpts.weibo.bean.UserList

object FriendsContract {

    interface IView {
        fun onActivityClose(view: View)
    }

    interface IViewModel {
        val userListData: LiveData<UserList>
        fun refresh()
        fun loadMore()
    }

    interface IModel {

        fun easyJob(index: Long): LiveData<Long>
    }
}