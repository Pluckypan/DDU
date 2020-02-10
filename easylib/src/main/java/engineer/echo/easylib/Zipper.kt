package engineer.echo.easylib

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * 输入流写入输出流
 */
private fun InputStream.writeTo(
    outputStream: OutputStream,
    bufferSize: Int = 1024 * 4
) {
    val buffer = ByteArray(bufferSize)
    val br = this.buffered()
    val bw = outputStream.buffered()
    var length = 0

    while ({ length = br.read(buffer);length != -1 }()) {
        bw.write(buffer, 0, length)
    }
    bw.flush()
}

/**
 * 解压例行检查 确保解压目录正常创建
 */
private fun checkUnzipFolder(path: String): Boolean {
    val file = File(path)

    //解压路径不能是文件
    if (file.isFile) return false

    //确保先创建解压目录
    if (!file.exists()) {
        if (!file.mkdirs()) return false
    }
    return true
}

// ---------------------- public ----------------------

fun File.smartCreateNewFile(): Boolean {
    if (exists()) return true
    if (parentFile.exists()) return createNewFile()
    if (parentFile.mkdirs()) {
        if (this.createNewFile()) {
            return true
        }
    }
    return false
}

/**
 * 解压到当前文件夹
 */
fun File.unZip() {
    this unZipTo (this.absolutePath.replace(".zip", ""))
}

/**
 * 解压文件到指定目录
 */
infix fun File.unZipTo(path: String): Boolean {
    if (!checkUnzipFolder(path)) return false
    ZipFile(this).use { zipFile ->
        return zipFile.unZipTo(path)
    }
}

/**
 * 解压文件到指定目录
 */
fun ZipFile.unZipTo(
    path: String,
    callback: ((total: Long, current: Long, progress: Int) -> Unit?)? = null
): Boolean {
    if (!checkUnzipFolder(path)) return false
    var makeDirFailed = false
    var index = 0L
    val total = this.size() * 1L
    for (entry in entries()) {
        if (entry.isDirectory) {
            //创建文件夹
            val parentFile = File("$path${File.separator}${entry.name}")
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                makeDirFailed = true
                //如果创建文件夹失败 则文件夹向下的文件都会解压失败 直接退出循环
                break
            }
        } else {
            //创建文件
            val outputFile = File("$path${File.separator}${entry.name}")
            if (!outputFile.exists()) outputFile.smartCreateNewFile()
            try {
                getInputStream(entry).use { input ->
                    outputFile.outputStream().use { output ->
                        input.writeTo(output, DEFAULT_BUFFER_SIZE)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        index++
        callback?.invoke(total, index, calculateProgress(index, total))
    }
    callback?.invoke(total, index, calculateProgress(index, total))
    return !makeDirFailed
}

private fun calculateProgress(index: Long, total: Long): Int {
    return if (total == 0L) 0 else (index * 100f / total).toInt()
}


/**
 * File为指定的输出文件路径 如 target.zip
 * @param sourcePath 指定需要被压缩的文件，可为路径、文件
 */
fun File.zip(vararg sourcePath: String): Boolean {
    ZipOutputStream(FileOutputStream(this)).use { output ->
        return try {
            output.zipFrom(*sourcePath)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

private fun ZipOutputStream.zipFrom(vararg folder: String) {
    this.use {
        val files = folder.map { File(it) }
        files.forEach {
            if (it.isFile) {
                zip(arrayOf(it), null)
            } else if (it.isDirectory) {
                zip(it.listFiles(), it.name)
            }
        }
    }
}

/**
 * 压缩文件
 */
private fun ZipOutputStream.zip(files: Array<File>, path: String?) {
    //前缀,用于构造路径
    val prefix = if (path == null) "" else "$path${File.separator}"
    if (files.isEmpty()) createEmptyFolder(prefix)
    files.forEach {
        if (it.isFile) {
            it.inputStream().buffered().use { ins ->
                try {
                    val entry = ZipEntry("$prefix${it.name}")
                    putNextEntry(entry)
                    ins.writeTo(this, DEFAULT_BUFFER_SIZE)
                    closeEntry()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            zip(it.listFiles(), "$prefix${it.name}")
        }
    }
}

/**
 * 生成一个压缩文件的文件夹
 */
private fun ZipOutputStream.createEmptyFolder(location: String) {
    putNextEntry(ZipEntry(location))
    closeEntry()
}