package engineer.echo.yi.ui.coroutines

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoroutinesViewModel(bundle: Bundle? = null) : ViewModel(), CoroutinesContract.IViewModel {

    private val model: CoroutinesContract.IModel = CoroutinesModel()

    private val jobTrigger = MutableLiveData<Long>()

    class Factory(private val bundle: Bundle?, app: Application) :
        ViewModelProvider.AndroidViewModelFactory(app) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CoroutinesViewModel(bundle) as T
        }

    }
}