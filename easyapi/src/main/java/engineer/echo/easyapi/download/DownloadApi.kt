package engineer.echo.easyapi.download

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Tag
import retrofit2.http.Url

interface DownloadApi {
    @Streaming
    @GET
    fun download(@Url url: String, @Tag path: String): LiveData<DownloadState>
}