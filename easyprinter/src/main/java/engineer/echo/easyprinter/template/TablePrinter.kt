package engineer.echo.easyprinter.template

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 *  TablePrinter.kt
 *  Info: 绘制简单表格
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/23 - 2:13 PM
 *  more about me: http://www.1991th.com
 */
class TablePrinter : IPainter() {

    private val headers = arrayListOf("商品名称", "尺码", "数量", "单价", "金额")
    private val items =
        arrayListOf(
            arrayListOf("Nike", "M", "10", "899", "8990"),
            arrayListOf("阿迪达斯", "L", "2", "390", "780"),
            arrayListOf("纽巴伦", "X", "1", "599", "599")
        )
    private var headerOffset = 0f
    private var rotation = 90f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.FILL
        textSize = 20f
        strokeWidth = 4f
    }

    fun isRotated(): Boolean {
        Math.abs(rotation).also {
            return it == 90f || it == 270f
        }
    }

    override fun draw(canvas: Canvas) {
        val cx = width / 2f
        val cy = height / 2f
        canvas.rotate(rotation, cx, cy)
        canvas.drawColor(Color.WHITE)
        titlePaint().apply {
            canvas.drawText("红火烧烤利郎店", cx, 120f, this)
        }

        val w = headers.size
        val h = items.size + 1
        val singleH = 80
        val startY = 220f
        val endY = startY + singleH * h
        val startX = 20f
        val endX = if (isRotated()) {
            height - 20f
        } else {
            width - 20f
        }
        val singleW = (endX - startX) / w

        // 画表格
        linePaint().apply {
            // 画竖线
            for (i in 0..w) {
                val x = (startX + i * singleW) * 1f
                canvas.drawLine(x, startY, x, endY, this)
            }
            // 画横线
            for (j in 0..h) {
                val y = (startY + j * singleH) * 1f
                canvas.drawLine(startX, y, endX, y, this)
            }
        }

        // 填标题
        headerPaint().apply {
            headers.forEachIndexed { i, s ->
                val cellX = startX + i * singleW + singleW / 2f
                val cellY = startY + singleH / 2f + headerOffset
                canvas.drawText(s, cellX, cellY, this)
            }
        }
        // 填内容
        itemPaint().apply {
            items.forEachIndexed { i, list ->
                list.forEachIndexed { j, s ->
                    val cellX = startX + j * singleW + singleW / 2f
                    val cellY = startY + (i + 1) * singleH + singleH / 2f + headerOffset
                    canvas.drawText(s, cellX, cellY, this)
                }
            }
        }

        // 画单号
        orderPaint().apply {
            canvas.drawText("单号:0x20190624", startX, startY - bottomOffset, this)
        }
        // 画时间
        timePaint().apply {
            canvas.drawText("2019-06-24 12:06:08", endX, startY - bottomOffset, this)
        }
        // 画备注
        memoPaint().apply {
            canvas.drawText(
                "温馨提示:adidas（阿迪达斯）创办于1949年.",
                startX,
                endY + topOffset,
                this
            )
        }
    }

    private fun titlePaint(): Paint {
        return paint.apply {
            textSize = 80f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
    }


    private fun linePaint(): Paint {
        return paint.apply {
            isFakeBoldText = false
            textAlign = Paint.Align.LEFT
        }
    }

    private fun headerPaint(): Paint {
        return paint.apply {
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            textSize = 46f
            if (headerOffset == 0f) {
                val fm = fontMetrics
                headerOffset = (fm.bottom - fm.top) / 2 - fm.bottom
            }
        }
    }

    private fun itemPaint(): Paint {
        return paint.apply {
            isFakeBoldText = false
            textAlign = Paint.Align.CENTER
            textSize = 46f
        }
    }

    private var bottomOffset = 0f

    private fun orderPaint(): Paint {
        return paint.apply {
            isFakeBoldText = true
            textAlign = Paint.Align.LEFT
            textSize = 46f
            if (bottomOffset == 0f) {
                bottomOffset = fontMetrics.bottom
            }
        }
    }

    private fun timePaint(): Paint {
        return paint.apply {
            isFakeBoldText = false
            textAlign = Paint.Align.RIGHT
            textSize = 46f
        }
    }

    private var topOffset = 0f

    private fun memoPaint(): Paint {
        return paint.apply {
            isFakeBoldText = false
            textAlign = Paint.Align.LEFT
            textSize = 46f
            if (topOffset == 0f) {
                val fm = fontMetrics
                val half = (fm.bottom - fm.top) / 2
                topOffset = half - fm.bottom + half
            }
        }
    }

}