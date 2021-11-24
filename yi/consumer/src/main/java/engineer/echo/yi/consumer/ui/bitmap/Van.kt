package engineer.echo.yi.consumer.ui.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntRange
import androidx.core.graphics.drawable.toBitmap
import engineer.echo.yi.consumer.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class Van @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val tplH by lazy {
        resources.getDrawable(R.drawable.template)
    }
    private val tplV by lazy {
        resources.getDrawable(R.drawable.result)
    }
    private val offset = 40f
    val vertical = AtomicBoolean(false)
    val type = AtomicBoolean(true)

    private val origin
        get() = if (vertical.get()) {
            tplV
        } else {
            tplH
        }.toBitmap()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 36f
        color = Color.parseColor("#330000FF")
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
        canvas.save()
        canvas.translate(40f, 40f)
        super.onDraw(canvas)
        if (isInEditMode) {
            canvas.drawText("Van", origin.width / 2f, origin.height / 2f, paint)
            return
        }
        val fusionSize = 60f;(min(poster.worldWidth, poster.worldHeight) * 0.1f).minEdge()
        val py = 0f
        val ry = py + 20f
        val b1 = origin.fusionEdge(poster, fusionSize)
        canvas.drawRect(0f, ry, b1.width * 1f, ry + b1.height, paint)
        canvas.drawBitmap(b1, 0f, ry, null)
        val tx = b1.width * 0.5f
        val ty = py + b1.height + 60
        canvas.drawText(
            "f=$fusionSize dest -> ${b1.width}*${b1.height}",
            tx,
            ty,
            paint
        )
        canvas.drawText(
            "f=$fusionSize src -> ${origin.width}*${origin.height}",
            tx,
            ty + 40,
            paint
        )
        canvas.restore()
    }

    companion object {
        private fun Float.minEdge(): Float {
            return min(20f, this)
        }

        fun saveBitmap2SD(bmp: Bitmap, filePath: String, format: CompressFormat): Boolean {
            val file = File(filePath)
            try {
                file.createNewFile()
            } catch (e: IOException) {
                return false
            }
            val fOut: FileOutputStream?
            fOut = try {
                FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                return false
            }
            bmp.compress(format, 100, fOut)
            try {
                fOut.flush()
                fOut.close()
            } catch (e: IOException) {
                return false
            }
            return true
        }

        /**
         * this 为原始用户所见即所得的画面，需要缩放空出融合边界
         */
        private fun Bitmap.fusionEdge(
            patch: PosterPhotoPatch,
            fusionSize: Float
        ): Bitmap {
            val originWidth = this.width
            val originHeight = this.height
            // 取画布短边的 10% 作为融合的边界

            // 上下左右的分布
            val hasLeft = patch.neighborDistribution[0]
            val hasTop = patch.neighborDistribution[1]
            val hasRight = patch.neighborDistribution[2]
            val hasBottom = patch.neighborDistribution[3]
            // 等比缩放后，需要填充的宽高
            val top = if (hasTop) fusionSize else 0f
            val bottom = if (hasBottom) fusionSize else 0f
            val left = if (hasLeft) fusionSize else 0f
            val right = if (hasRight) fusionSize else 0f
            // 最终需要裁切的宽高
            val clipW = originWidth + left + right
            val clipH = originHeight + top + bottom
            // 图片需要缩放的比例
            val factor = max(clipW / originWidth, clipH / originHeight)
            // 等比缩放完的图片大小
            val scaleWidth = originWidth * factor
            val scaleHeight = originHeight * factor
            // 等比缩放完，与实际裁切大小，一定一边有剩余
            val diffX = scaleWidth - clipW
            val diffY = scaleHeight - clipH
            // 横向裁切位置
            val startX: Float = if ((hasLeft && hasRight) || (!hasLeft && !hasRight)) {
                diffX / 2f
            } else if (hasLeft) {
                diffX
            } else {
                0f
            }
            // 纵向裁切位置
            val startY: Float = if ((hasTop && hasBottom) || (!hasTop && !hasBottom)) {
                diffY / 2f
            } else if (hasTop) {
                diffY
            } else {
                0f
            }
            // 等比缩放图
            return Bitmap.createScaledBitmap(
                this,
                scaleWidth.roundToInt(),
                scaleHeight.roundToInt(),
                true
            ).let {
                // 起始位置以及裁切大小
                val sx = startX.roundToInt()
                val sy = startY.roundToInt()
                val cw = clipW.roundToInt()
                val ch = clipH.roundToInt()
                // 四舍五入会越界，判断一下
                val ow = if (sx + cw > it.width) {
                    it.width - sx
                } else {
                    cw
                }
                val oh = if (sy + ch > it.height) {
                    it.height - sy
                } else {
                    ch
                }
                // 最终渲染图
                Bitmap.createBitmap(
                    it,
                    sx,
                    sy,
                    ow,
                    oh,
                    null,
                    true
                )
            }
        }
    }
}