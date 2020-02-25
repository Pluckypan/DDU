package engineer.echo.yi.consumer.ui.friends

import android.os.SystemClock
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors

class FriendsModel : FriendsContract.IModel {

    private val workExecutor by lazy {
        Executors.newCachedThreadPool()
    }

    private val liveData by lazy {
        MutableLiveData<Long>()
    }


    override fun easyJob(index: Long): LiveData<Long> {
        if (index <= 0) return liveData
        workExecutor.execute {
            val start = SystemClock.uptimeMillis()
            for (i in 0..index) {
                easyJobImpl(start).also {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    @WorkerThread
    fun easyJobImpl(start: Long): Long {
        SystemClock.sleep(1000)
        return SystemClock.uptimeMillis() - start
    }
}