package engineer.echo.easylib

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock
import android.renderscript.*
import android.util.Base64
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.graphics.scale
import java.io.*


/**
 *  BitmapX.kt
 *  Info: Bitmap相关扩展
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/12 - 2:09 PM
 *  more about me: http://www.1991th.com
 */
fun Bitmap.toInputStream(quality: Int, format: CompressFormat = CompressFormat.JPEG): InputStream {
    val bos = ByteArrayOutputStream()
    this.compress(format, quality, bos)
    return ByteArrayInputStream(bos.toByteArray())
}


fun Bitmap.toFile(
    filePath: String,
    quality: Int = 100,
    format: CompressFormat = CompressFormat.JPEG
): Boolean {
    val file = File(filePath)
    return try {
        file.createNewFile()
        FileOutputStream(file).use {
            compress(format, quality, it)
        }
        true
    } catch (e: Exception) {
        Log.e("BitmapX", e.message)
        false
    }
}

fun Bitmap.toBase64(format: CompressFormat = CompressFormat.PNG, quality: Int = 100): String {
    val bStream = ByteArrayOutputStream()
    this.compress(format, quality, bStream)
    val bytes = bStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun Bitmap.toNonPremultiplied(): Bitmap? {
    return if (!isPremultiplied) {
        this
    } else {
        /**
         * 如果图片存在预乘 则需要去预乘 否则存在投影的图片完全不对(失真)
         * Android 关于预乘的说明详见 [Bitmap.setPremultiplied]
         * Bitmaps are always treated as pre-multiplied by the view system and
         * Canvas for performance reasons.
         * 如果去除预乘，那么 [Canvas.drawBitmap] 、[Bitmap.createBitmap] 则会Crash 但并不影响保存图片
         * 详见 [Bitmap.isPremultiplied]
         * Only pre-multiplied bitmaps may be drawn by the view system or
         * Canvas. If a non-pre-multiplied bitmap with an alpha channel is
         * drawn to a Canvas, a RuntimeException will be thrown.
         */
        BitmapFactory.decodeStream(
            this.toInputStream(100, CompressFormat.PNG),
            null,
            BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
                inPremultiplied = false
                inScaled = true

            })
    }
}

fun Bitmap.scaleLossLess(scale: Float, app: Application): Bitmap {
    if (scale <= 0 || scale == 1f) return this
    val before = SystemClock.uptimeMillis()
    // 在高倍缩小的情况下建议使用 高斯算法(显示效果会比较好,其他情况下可能比较糟糕)
    val bitmap = if (scale < 0.55f) {
        // scaleDown 阈值小于 0.55f 则使用高斯算法
        this.gaussScale(scale, 1.0f, app)
    } else {
        // 否则使用 Android API
        this.scale((width * scale).toInt(), (height * scale).toInt())
    }
    Log.d("BitmapX", "scaleLossLess scale=$scale cost=${SystemClock.uptimeMillis() - before}")
    return bitmap
}

/**
 * 通过高斯算法缩放图片
 * 通过 ffmpeg -i 708721762.png -s 176x44 -sws_flags gauss  -y 0.3.png 对比
 * 发现 gauss & bilinear 缩小图片时 图片质量丢失最小
 * 参考链接：
 * https://medium.com/@petrakeas/alias-free-resize-with-renderscript-5bf15a86ce3
 * https://blog.csdn.net/leixiaohua1020/article/details/84483277
 * http://www.echo.engineer/c/ffmpeg.html
 */
fun Bitmap.gaussScale(scale: Float, radiusOffset: Float, app: Application): Bitmap {
    // 创建 RenderScript 引擎
    val renderScriptEngine = RenderScript.create(app)

    val resizeRatio = 1f / scale
    /**
     * 计算高斯模糊半径,参考源代码
     * https://android.googlesource.com/platform/frameworks/rs/+/master/cpu_ref/rsCpuIntrinsicBlur.cpp
     */
    val sigma = resizeRatio / Math.PI.toFloat()
    //+ radiusOffset 的目的是更柔和一点 否则字体容易出现锯齿
    var radius = 2.5f * sigma - 1.5f + radiusOffset
    radius = 25f.coerceAtMost(0.0001f.coerceAtLeast(radius))

    /**
     * 由于 gauss & bilinear 两种缩放算法表现比较好且 Android 已经自带了 ScriptIntrinsicBlur (基于gauss)
     * 所以暂时使用 ScriptIntrinsicBlur 不自行 写 renderScript 脚本
     * 如果要自行实现 gauss or bilinear or 其他缩放算法
     * 可参考：http://www.echo.engineer/c/renderscript.html
     */

    // 输入流
    val allIn = Allocation.createFromBitmap(renderScriptEngine, this)
    val blurScript = ScriptIntrinsicBlur.create(renderScriptEngine, allIn.element)
    // 输出暂存
    val tmpAlloc = Allocation.createTyped(renderScriptEngine, allIn.type)
    blurScript.setRadius(radius)
    blurScript.setInput(allIn)
    blurScript.forEach(tmpAlloc)
    // 释放
    allIn.destroy()
    blurScript.destroy()

    // 缩放
    val srcAspect = width.toFloat() / height
    val dstWidth = (width * scale + 0.5f).toInt()
    val dstHeight = (dstWidth / srcAspect).toInt()
    val dstBitmap = Bitmap.createBitmap(dstWidth, dstHeight, this.config)
    // 输出流 在原有高斯编码的基础上做缩放
    val allOut = Allocation.createTyped(
        renderScriptEngine,
        Type.createXY(renderScriptEngine, tmpAlloc.element, dstWidth, dstHeight)
    )
    // 缩放
    val resizeScript = ScriptIntrinsicResize.create(renderScriptEngine)
    resizeScript.setInput(tmpAlloc)
    resizeScript.forEach_bicubic(allOut)
    // 将最终数据输出到 Bitmap
    allOut.copyTo(dstBitmap)

    // 释放
    tmpAlloc.destroy()
    allOut.destroy()
    resizeScript.destroy()
    renderScriptEngine.destroy()
    return dstBitmap
}

/**
 * 去除边缘透明像素 offset 均为正 如果透明像素较多 比较耗时
 * @param offsetL 左边预留像素
 * @param offsetT 顶部预留像素
 * @param offsetR 右边预留像素
 * @param offsetB 底部预留像素
 */
@WorkerThread
fun Bitmap.clipTransparentEdge(offsetL: Int = 0,
                               offsetT: Int = 0,
                               offsetR: Int = 0,
                               offsetB: Int = 0): Bitmap {
    val before = SystemClock.uptimeMillis()
    var left = 0
    var top = 0
    var right = width
    var bottom = height

    // 从边缘检测(比较快)
    // LEFT
    for (i in 0 until width) {
        var find = false
        for (j in 0 until height) {
            val color = getPixel(i, j)
            // 如果不是透明像素
            if (Color.alpha(color) != 0) {
                left = i
                find = true
                break
            }
        }
        if (find) {
            break
        }
    }
    // TOP
    for (i in 0 until height) {
        var find = false
        for (j in 0 until width) {
            val color = getPixel(j, i)
            if (Color.alpha(color) != 0) {
                top = i
                find = true
                break
            }
        }
        if (find) {
            break
        }
    }

    // RIGHT
    for (i in width - 1 downTo 0) {
        var find = false
        for (j in 0 until height) {
            val color = getPixel(i, j)
            if (Color.alpha(color) != 0) {
                right = i
                find = true
                break
            }
        }
        if (find) {
            break
        }
    }

    // BOTTOM
    for (i in height - 1 downTo 0) {
        var find = false
        for (j in 0 until width) {
            val color = getPixel(j, i)
            if (Color.alpha(color) != 0) {
                bottom = i
                find = true
                break
            }
        }
        if (find) {
            break
        }
    }

    left -= offsetL
    top -= offsetT
    right += offsetR
    bottom += offsetB
    if (left < 0) left = 0
    if (top < 0) top = 0
    if (right > width) right = width
    if (bottom > height) bottom = height

    return if (left == 0 && top == 0 && right == width && bottom == height) {
        this
    } else {
        Log.d("BitmapX", "clipTransparentEdge {$width,$height},cut={$left,$top,${width - right},${height - bottom}} cost=${SystemClock.uptimeMillis() - before}")
        Bitmap.createBitmap(this, left, top, right - left, bottom - top)
    }
}