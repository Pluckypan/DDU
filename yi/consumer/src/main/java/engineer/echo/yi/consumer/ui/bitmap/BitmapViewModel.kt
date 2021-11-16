package engineer.echo.yi.consumer.ui.bitmap

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

class BitmapViewModel(bundle: Bundle? = null) : ViewModel(), BitmapContract.IViewModel {

    private val model: BitmapContract.IModel = BitmapModel()

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
            return BitmapViewModel(bundle) as T
        }

    }
}