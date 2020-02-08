package engineer.echo.yi.producer.cmpts.zip

import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobApi

@JobApi(uniqueId = "Zip@Producer", retrofit = true)
interface ZipApi {

    fun zip(source: String, target: String): Result
}