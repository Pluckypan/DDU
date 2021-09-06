package engineer.echo.study.cmpts.apm

import android.util.Log
import android.view.Choreographer
import java.util.concurrent.atomic.AtomicLong

class FpsHelper : Choreographer.FrameCallback {

    private val lastFrameTime = AtomicLong()
    private var frameThreshold = 30

    // 1s 60帧 对应 1帧 16.67ms
    private val frameRerNanoTime = (1000_000_000f / 60f).toLong()

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTime.get() == 0L) {
            lastFrameTime.set(frameTimeNanos)
        }
        // 连续两帧之间的时间差
        val diff = frameTimeNanos - lastFrameTime.get()
        if (diff >= frameRerNanoTime) {
            val droppedFrames = diff / frameRerNanoTime
            if (droppedFrames > frameThreshold) {
                Log.i(
                    TAG,
                    "droppedFrames = " + droppedFrames
                            + "The application may be doing too much work on its main thread."
                )
            }
        }
        lastFrameTime.set(frameTimeNanos)
        // 循环处理
        Choreographer.getInstance().postFrameCallback(this)
    }

    fun start() {
        Choreographer.getInstance().postFrameCallback(this)
    }

    fun stop() {
        Choreographer.getInstance().removeFrameCallback(this)
    }

    companion object {
        private const val TAG = "FpsHelper"
    }

}