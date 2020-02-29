package engineer.echo.easyapi

import androidx.lifecycle.MutableLiveData

internal class EasyLiveData<T>(var id: String = "") : MutableLiveData<T>()