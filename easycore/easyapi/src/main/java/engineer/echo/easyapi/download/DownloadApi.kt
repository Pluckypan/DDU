package engineer.echo.easyapi.download

import androidx.lifecycle.LiveData
import retrofit2.http.*

interface DownloadApi {
    @Streaming
    @GET
    fun download(@Header("RANGE") start: String, @Url url: String, @Tag path: String): LiveData<DownloadState>
}