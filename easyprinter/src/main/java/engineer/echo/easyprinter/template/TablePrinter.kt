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

    private val headers = arrayListOf<String>()
    private val items = arrayListOf<ArrayList<String>>()
    private var title: String = ""
    private var orderId: String = ""
    private var time: String = ""
    private var memo: String = ""
    private var headerOffset = 0f
    var rotation = 90f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.FILL
        textSize = 20f
        strokeWidth = 4f
    }

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    fun setup(config: TableConfig) {
        headers.clear()
        if (config.headers.isNotEmpty()) {
            headers.addAll(config.headers)
        }
        items.clear()
        if (config.items.isNotEmpty()) {
            items.addAll(config.items)
        }
        title = config.title
        orderId = config.orderId
        time = config.time
        memo = config.memo
        rotation = config.rotation
    }

    override fun draw(canvas: Canvas) {
        val cx = width / 2f
        val cy = height / 2f
        canvas.rotate(rotation, cx, cy)

        val w = headers.size
        val h = items.size + 1
        val singleH = 80
        val startY = 220f
        val endY = startY + singleH * h
        val startX = 20f
        val endX = width - 20f
        val singleW = (endX - startX) / w

        canvas.drawRect(startX, 0f, endX, endY + 70, bgPaint)
        titlePaint().apply {
            canvas.drawText(title, cx, 120f, this)
        }

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
            canvas.drawText(orderId, startX, startY - bottomOffset, this)
        }
        // 画时间
        timePaint().apply {
            canvas.drawText(time, endX, startY - bottomOffset, this)
        }
        // 画备注
        memoPaint().apply {
            canvas.drawText(
                memo,
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