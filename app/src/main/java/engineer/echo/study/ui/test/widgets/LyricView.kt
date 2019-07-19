package engineer.echo.study.ui.test.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import engineer.echo.study.R

/**
 *  LyricView.kt
 *  Info: 歌词逐字显示控件
 *  Created by Plucky(plucky@echo.engineer) on 2019/7/19 - 2:01 PM
 *  more about me: http://www.1991th.com
 */
class LyricView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {
    private val layer0 = ContextCompat.getDrawable(context, R.drawable.img_lyric_00000)
    private val layer1 = ContextCompat.getDrawable(context, R.drawable.img_lyric_00001)

    private val layer2 = BitmapFactory.decodeResource(resources, R.drawable.img_lyric_00002)
    private val layer3 = BitmapFactory.decodeResource(resources, R.drawable.img_lyric_00003)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private val mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    var progress: Float = 0.0f
    val mSrcRect = Rect()
    val mDstRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var offsety = 0f
        layer0?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)
        }
        layer1?.apply {
            canvas.saveLayer(0f, 0f, intrinsicWidth * 1f, intrinsicHeight * 1f, null)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)
            canvas.drawRect(intrinsicWidth * progress, 0f, intrinsicWidth * 1f, intrinsicHeight * 1f, mPaint)
            canvas.restore()
            offsety = intrinsicHeight * 1f
        }

        val bLeft = (width - layer2.width) * 1f
        canvas.drawBitmap(layer2, bLeft, offsety, null)
        mSrcRect.set(0, 0, (layer3.width * progress).toInt(), layer3.height)
        mDstRect.set(bLeft, offsety, layer3.width * progress + bLeft, layer3.height + offsety)
        canvas.drawBitmap(layer3, mSrcRect, mDstRect, null)
    }
}