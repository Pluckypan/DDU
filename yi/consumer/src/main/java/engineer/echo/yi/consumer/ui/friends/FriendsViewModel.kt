package engineer.echo.yi.consumer.ui.friends

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import engineer.echo.easyapi.EasyApi
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.cmpts.weibo.api.Users
import engineer.echo.yi.consumer.cmpts.weibo.bean.UserList

class FriendsViewModel(bundle: Bundle? = null) : ViewModel(), FriendsContract.IViewModel {

    private val _uid = Weibo.getUser().id
    private val uid = bundle?.getString(FriendsActivity.KEY_UID, _uid) ?: _uid

    private val model: FriendsContract.IModel = FriendsModel()

    private val refreshTrigger = MutableLiveData<String>()


    override val userListData: LiveData<UserList> = Transformations.switchMap(refreshTrigger) {
        EasyApi.create(Users::class.java).getFollowing(it, 20, getNextCursor())
    }

    init {
        refresh()
    }

    override fun refresh() {
        refreshTrigger.value = uid
    }

    private fun getLastData(): UserList? = userListData.value

    private fun getPreCursor(): Int {
        return getLastData()?.previousCursor ?: 0
    }

    private fun getNextCursor(): Int {
        return getLastData()?.nextCursor ?: 0
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(private val bundle: Bundle?, app: Application) :
        ViewModelProvider.AndroidViewModelFactory(app) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FriendsViewModel(bundle) as T
        }

    }
}