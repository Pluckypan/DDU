package $currentPackage$

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

class $moduleName$ViewModel(bundle: Bundle? = null) : ViewModel(), $moduleName$Contract.IViewModel {

    private val model: $moduleName$Contract.IModel = $moduleName$Model()

    private val jobTrigger = MutableLiveData<Long>()

    override fun calculate(index: Int) {
        jobTrigger.value = index * 1L
    }

    override val easyJobData: LiveData<Long> = Transformations.switchMap(jobTrigger) {
        model.easyJob(it)
    }

    init {
        calculate(bundle?.size() ?: 5)
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(private val bundle: Bundle?, app: Application) :
        ViewModelProvider.AndroidViewModelFactory(app) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return $moduleName$ViewModel(bundle) as T
        }

    }
}