package engineer.echo.study.mvvm.easy

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

class EasyViewModel(bundle: Bundle? = null) : ViewModel(), EasyContract.IViewModel {

    private val model: EasyContract.IModel = EasyModel()

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
            return EasyViewModel(bundle) as T
        }

    }
}