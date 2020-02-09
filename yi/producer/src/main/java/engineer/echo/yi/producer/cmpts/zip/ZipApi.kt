package engineer.echo.yi.producer.cmpts.zip

import engineer.echo.easyapi.ProgressResult
import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobApi
import engineer.echo.easyapi.annotation.JobCallback

@JobApi(uniqueId = "Zip@Producer", retrofit = true)
interface ZipApi {

    fun zip(source: String, target: String, i: Int, b: Boolean, f: Float, l: Long): Result

    fun unzip(source: String, target: String): Result

    fun unzipProgress(source: String, target: String, listener: JobCallback): ProgressResult
}