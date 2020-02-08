package engineer.echo.yi.producer.cmpts.zip

import androidx.lifecycle.LiveData
import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.job.EasyJob
import engineer.echo.easylib.unZipTo
import engineer.echo.easylib.zip
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.zip.ZipFile

sealed class ZipHelper {

    interface ZipApi1 {

        @GET("zip")
        @EasyJob
        fun zip(@Query("source") source: String, @Query("target") target: String): LiveData<Result>

        @GET("unzip")
        @EasyJob
        fun unzip(@Query("source") source: String, @Query("target") target: String): LiveData<ProgressResult>
    }

    fun zip(source: String, target: String): Boolean {
        return File(target).zip(source)
    }

    fun unzip(source: String, target: String): Boolean {
        return ZipFile(source).unZipTo(target)
    }
}