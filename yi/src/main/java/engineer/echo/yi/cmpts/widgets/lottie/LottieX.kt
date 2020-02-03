package engineer.echo.yi.cmpts.widgets.lottie

import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import engineer.echo.yi.YiApp


fun String.preloadLottie() {
    try {
        LottieCompositionFactory.fromUrl(YiApp.getApp(), this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun LottieAnimationView.loadUrlResource(url: String) {
    try {
        setFailureListener {
            it.printStackTrace()
        }
        setAnimationFromUrl(url)
    } catch (ignore: Exception) {
        ignore.printStackTrace()
    }
}