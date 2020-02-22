package engineer.echo.yi.im.ui.kotlin

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

class KotlinViewModel(bundle: Bundle? = null) : ViewModel(), KotlinContract.IViewModel {

    private val model: KotlinContract.IModel = KotlinModel()

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
            return KotlinViewModel(bundle) as T
        }

    }
}