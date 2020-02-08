package engineer.echo.yi.producer.cmpts.zip

import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.easylib.zip
import java.io.File

@JobServer(uniqueId = "Zip@Producer")
class ZipServer : ZipApi {

    override fun zip(source: String, target: String): Result {
        return Result(if (File(target).zip(source)) null else Exception("zip failed"))
    }

}