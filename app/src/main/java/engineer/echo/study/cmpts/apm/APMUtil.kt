package engineer.echo.study.cmpts.apm

import android.os.Debug
import java.io.File

/**
 * 每个进程可以创建的 file descriptors 不能超过1024个
 * FileInputStream，FileOutputStream，FileReader，FileWriter 如果创建未关闭会造成内存泄露、也可能会 fd 泄露
 * fd 泄漏 会打印 Too many open files
 * CursorWindow max = 2MB 的实质是共享内存的抽象，以实现跨进程数据共享。共享内存所採用的实现方式是文件映射
 * Java在起线程的时候也会需要开Fd资源
 */
object APMUtil {

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            print("thread=${thread.name} exception=$exception")
        }
    }

    fun fileCount(path: String?): Int {
        if (path.isNullOrEmpty()) {
            return 0
        }
        return try {
            File(path).list().size
        } catch (t: Throwable) {
            t.printStackTrace()
            0
        }
    }

    val nativeHeapSize: Long
        get() = Debug.getNativeHeapSize()

    val nativeFreeHeapSize: Long
        get() = Debug.getNativeHeapFreeSize()

    val nativeAllocatedHeapSize: Long
        get() = Debug.getNativeHeapAllocatedSize()

    val allThreadCount: Int
        get() = fileCount("/proc/self/task")

    val allFdCount: Int
        get() = fileCount("/proc/self/fd")

    val javaThreadCount: Int
        get() = Thread.activeCount()

    val nativeThreadCount: Int
        get() = allThreadCount - javaThreadCount

    val javaTotalMemory: Long
        get() = Runtime.getRuntime().totalMemory()

    val javaFreeMemory: Long
        get() = Runtime.getRuntime().freeMemory()

    /**
     * Java 内存超出这个限制会 OOM
     */
    val javaMaxMemory: Long
        get() = Runtime.getRuntime().maxMemory()


}