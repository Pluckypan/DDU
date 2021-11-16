package engineer.echo.yi.consumer.ui.bitmap

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntRange
import androidx.core.graphics.drawable.toBitmap
import engineer.echo.yi.consumer.R
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min
import kotlin.math.roundToInt

class Van @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val tplH by lazy {
        resources.getDrawable(R.drawable.template)
    }
    private val tplV by lazy {
        resources.getDrawable(R.drawable.template_v)
    }
    private val offset = 40f
    val vertical = AtomicBoolean(false)

    private val origin
        get() = if (vertical.get()) {
            tplV
        } else {
            tplH
        }.toBitmap()

    private val mtx = Matrix()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 36f
        color = Color.parseColor("#88fff143")
    }
    val poster by lazy {
        PosterPhotoPatch(width, height, BooleanArray(4) {
            it >= 0
        })
    }

    fun update(@IntRange(from = 0, to = 3) index: Int, value: Boolean) {
        poster.neighborDistribution[index] = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            canvas.drawText("Van", origin.width / 2f, origin.height / 2f, paint)
            return
        }
        canvas.drawBitmap(origin, 0f, 0f, null)
        canvas.drawText("origin", offset, offset * 2, paint)

        val diff = (min(width, height) * 0.1f).minEdge()
        // 加高度、宽度会有剩余
        val fusionHeight = origin.height + 2 * diff
        val ratio = fusionHeight / origin.height
        val fusionWidth = fusionHeight * origin.width / origin.height
        val diffWidth = (fusionWidth - origin.width - 2 * diff) / 2f

        val py = origin.height + 20f
        // canvas.drawRect(0f, py, fusionWidth, py + fusionHeight, paint)
        mtx.postScale(ratio, ratio)
        mtx.postTranslate(0f, py)
        canvas.drawBitmap(origin, mtx, null)
        // 竖条
        canvas.drawRect(diffWidth, py, diffWidth + diff, py + fusionHeight, paint)
        canvas.drawRect(
            fusionWidth - diffWidth - diff,
            py,
            fusionWidth - diffWidth,
            py + fusionHeight,
            paint
        )
        // 横条
        canvas.drawRect(diffWidth, py, fusionWidth - diffWidth, py + diff, paint)
        canvas.drawRect(
            diffWidth,
            py + fusionHeight - diff,
            fusionWidth - diffWidth,
            py + fusionHeight,
            paint
        )
        mtx.reset()

        val ry = py + fusionHeight + 20f
        val b1 = origin.fusionEdge(poster, paint, true)
        canvas.drawBitmap(b1, 0f, ry, null)
        val b2 = origin.fusionEdge(poster, paint, false)
        val b2y = ry + b1.height + 20
        canvas.drawBitmap(b2, 0f, b2y, null)
    }

    companion object {

        private fun Float.minEdge(): Float {
            return min(20f, this)
        }

        /**
         * this 为原始用户所见即所得的画面，需要缩放空出融合边界
         * 以短边为主，否则「等比缩放」后，短边没有足够的「融合宽度」
         * 调试模式下，把「融合区域」给画下来
         * 因为要绘制调试边缘区域，所以不能用 Bitmap.createBitmap(result, 0, 0, patchWidth, patchHeight, matrix, false)
         * 用 Canvas & Matrix 步骤，放大 --> 裁剪 --> 输出
         */
        private fun Bitmap.fusionEdge(
            patch: PosterPhotoPatch,
            paint: Paint,
            debug: Boolean
        ): Bitmap {
            val originWidth = this.width
            val originHeight = this.height
            // 取画布短边的 10% 作为融合的边界
            val fusionSize = (min(patch.worldWidth, patch.worldHeight) * 0.1f).minEdge()
            // 上下左右的分布
            val hasLeft = patch.neighborDistribution[0]
            val hasTop = patch.neighborDistribution[1]
            val hasRight = patch.neighborDistribution[2]
            val hasBottom = patch.neighborDistribution[3]

            val matrix = Matrix()
            val ratio: Float
            var startX: Float
            var startY: Float
            val scaleWidth: Float
            val scaleHeight: Float
            var fusionWidth: Float
            var fusionHeight: Float
            // 基本参数
            if (originWidth < originHeight) {
                // 宽度较小的情况下，高度会有多余的「量」，先把多余的量去除掉
                scaleWidth = originWidth + 2 * fusionSize
                scaleHeight = scaleWidth * originHeight / originWidth
                ratio = scaleWidth * 1f / originWidth
                val diffY = (scaleHeight - originHeight - 2 * fusionSize) / 2f
                fusionWidth = scaleWidth
                fusionHeight = scaleHeight - 2 * diffY
                startX = 0f
                startY = diffY
            } else {
                // 高度较小的情况下，宽度会有多余的「量」，先把多余的量去除掉
                scaleHeight = originHeight + 2 * fusionSize
                scaleWidth = scaleHeight * originWidth / originHeight
                ratio = scaleHeight * 1f / originHeight
                val diffX = (scaleWidth - originWidth - 2 * fusionSize) / 2f
                fusionWidth = scaleWidth - 2 * diffX
                fusionHeight = scaleHeight
                startX = diffX
                startY = 0f
            }

            // 确认上下左右边缘
            if (!hasLeft) {
                fusionWidth -= fusionSize
                startX += fusionSize
            }
            if (!hasRight) {
                fusionWidth -= fusionSize
            }

            if (!hasTop) {
                fusionHeight -= fusionSize
                startY += fusionSize
            }
            if (!hasBottom) {
                fusionHeight -= fusionSize
            }
            val result = Bitmap.createBitmap(
                scaleWidth.roundToInt(),
                scaleHeight.roundToInt(),
                Bitmap.Config.ARGB_8888
            )
            // 绘制最终展示的区域
            val canvas = Canvas(result)
            canvas.drawColor(Color.parseColor("#66FA2855"))
            matrix.postScale(ratio, ratio)
            canvas.clipRect(startX, startY, startX + fusionWidth, startY + fusionHeight)
            canvas.drawBitmap(this, matrix, null)

            // 绘制 debug 区域
            if (hasLeft) {
                canvas.drawRect(startX, startY, startX + fusionSize, startY + fusionHeight, paint)
            }
            if (hasTop) {
                canvas.drawRect(startX, startY, startX + fusionWidth, startY + fusionSize, paint)
            }
            if (hasRight) {
                canvas.drawRect(
                    startX + fusionWidth - fusionSize,
                    startY,
                    startX + fusionWidth,
                    startY + fusionHeight,
                    paint
                )
            }
            if (hasBottom) {
                canvas.drawRect(
                    startX,
                    startY + fusionHeight - fusionSize,
                    startX + fusionWidth,
                    startY + fusionHeight,
                    paint
                )
            }
            return if (debug) {
                result
            } else {
                Bitmap.createBitmap(
                    result,
                    startX.roundToInt(),
                    startY.roundToInt(),
                    fusionWidth.roundToInt(),
                    fusionHeight.roundToInt()
                )
            }
        }
    }
}