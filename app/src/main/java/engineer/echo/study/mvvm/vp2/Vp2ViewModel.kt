package engineer.echo.study.mvvm.vp2

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

class Vp2ViewModel(bundle: Bundle? = null) : ViewModel(), Vp2Contract.IViewModel {

    private val model: Vp2Contract.IModel = Vp2Model()

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
            return Vp2ViewModel(bundle) as T
        }

    }
}