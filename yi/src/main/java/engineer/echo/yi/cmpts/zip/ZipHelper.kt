package engineer.echo.yi.cmpts.zip

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.job.EasyJob
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object ZipHelper {

    interface ZipApi {

        @GET("/")
        @EasyJob
        fun zip(@Header("source") source: String, @Query("target") target: String): LiveData<Result>

        @GET("/")
        @EasyJob
        fun unzip(@Query("source") source: String, @Query("target") target: String): LiveData<ProgressResult>
    }
}