package engineer.echo.study.cmpts.apm

import android.util.Log
import android.view.Choreographer
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class FpsHelper : Choreographer.FrameCallback {

    private val lastFrameTime = AtomicLong()
    private val enable = AtomicBoolean(false)
    private var frameThreshold = 30

    // 1s 60帧 对应 1帧 16.67ms
    private val frameRerNanoTime = (1000_000_000f / 60f).toLong()

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTime.get() == 0L) {
            lastFrameTime.set(frameTimeNanos)
        }
        // 连续两帧之间的时间差
        val diff = frameTimeNanos - lastFrameTime.get()
        val droppedFrames = if (diff >= frameRerNanoTime) diff / frameRerNanoTime else 0
        if (DEBUG_MODE.get()) {
            Log.i(TAG, "doFrame x1 droppedFrames = $droppedFrames")
        }
        if (droppedFrames > frameThreshold) {
            Log.i(
                TAG,
                "x2 droppedFrames = " + droppedFrames
                        + " The application may be doing too much work on its main thread."
            )
        }
        lastFrameTime.set(frameTimeNanos)
        // 循环处理
        if (enable.get()) {
            Choreographer.getInstance().postFrameCallback(this)
        } else {
            Choreographer.getInstance().removeFrameCallback(this)
            lastFrameTime.set(0)
        }
    }

    fun start() {
        enable.set(true)
        Choreographer.getInstance().postFrameCallback(this)
    }

    fun stop() {
        enable.set(false)
    }

    companion object {
        private const val TAG = "FpsHelper"
        val DEBUG_MODE = AtomicBoolean(true)
    }

}