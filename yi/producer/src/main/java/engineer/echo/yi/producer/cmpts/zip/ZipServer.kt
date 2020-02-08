package engineer.echo.yi.producer.cmpts.zip

import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.easylib.unZipTo
import engineer.echo.easylib.zip
import java.io.File
import java.util.zip.ZipFile

@JobServer(uniqueId = "Zip@Producer")
class ZipServer : ZipApi {

    override fun zip(
        source: String,
        target: String,
        i: Int,
        b: Boolean,
        f: Float,
        l: Long
    ): Result {
        return Result(if (File(target).zip(source)) null else Exception("zip failed"))
    }

    override fun unzip(source: String, target: String): Result {
        return Result(if (ZipFile(source).unZipTo(target)) null else Exception("unzip failed"))
    }
}