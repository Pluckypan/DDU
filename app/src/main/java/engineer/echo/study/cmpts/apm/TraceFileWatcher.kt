package engineer.echo.study.cmpts.apm

import android.os.FileObserver
import android.util.Log

class TraceFileWatcher : FileObserver(ANR_FILE, MASK) {

    fun start() {
        startWatching()
    }

    fun stop() {
        stopWatching()
    }

    override fun onEvent(event: Int, path: String?) {
        if (path.isNullOrEmpty().not()) {
            val filepath = ANR_FILE.plus(path)
            if (filepath.contains("trace")) {
                Log.i(TAG, "onEvent event=$event path=$path")
            }
        }
    }

    companion object {
        private const val TAG = "TraceFileWatcher"
        private const val ANR_FILE = "/data/anr/"
        private const val MASK = CLOSE_WRITE
    }
}