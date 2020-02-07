package engineer.echo.yi.common.cpmts.widgets.refresh.lottie

import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import engineer.echo.yi.common.EasyApp


fun String.preloadLottie() {
    try {
        LottieCompositionFactory.fromUrl(EasyApp.getApp(), this)
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