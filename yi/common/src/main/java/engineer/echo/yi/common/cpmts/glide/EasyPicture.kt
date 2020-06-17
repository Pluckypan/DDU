package engineer.echo.yi.common.cpmts.glide

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import engineer.echo.yi.common.EasyApp

@WorkerThread
fun Int.loadImage(width: Int = Int.MIN_VALUE, height: Int = Int.MIN_VALUE): Bitmap? {
    // EasyPicture.with(EasyApp.getApp()).asBitmap().submit(width, height).get()
    return EasyPicture.with(EasyApp.getApp()).asBitmap().submit(width, height).let {
        try {
            it.get()
        } catch (e: Exception) {
            null
        }
    }
}

