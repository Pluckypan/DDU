package engineer.echo.easyapi.pub

import java.io.File

fun File.tryCreateFileException(): Exception? {
    if (!exists()) {
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        try {
            createNewFile()
        } catch (e: Exception) {
            return e
        }
    }
    return null
}

fun File.tryCreateFile(): Boolean {
    return tryCreateFileException() == null
}