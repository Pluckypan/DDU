package engineer.echo.yi.producer.cmpts.zip

import engineer.echo.easyapi.Result
import engineer.echo.easyapi.annotation.JobCallback
import engineer.echo.easyapi.annotation.JobServer
import engineer.echo.easyapi.annotation.State
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

    override fun unzipProgress(
        source: String,
        target: String,
        listener: JobCallback
    ): ZipState {
        return ZipState().apply {
            exception =
                if (ZipFile(source).unZipTo(target) { total, index, progress ->
                        listener.onJobState(State.OnProgress, total, index, progress)
                    }) null else Exception("unzip progress failed")
            msg = if (exception == null) "success!kind of~" else "failed omg~~~"
        }
    }
}