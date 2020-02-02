package engineer.echo.yi.cmpts.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import engineer.echo.easylib.externalCache
import engineer.echo.yi.YiApp
import java.io.File

@GlideModule(glideName = "ImageYi")
class YiGlideModule : AppGlideModule() {

    companion object {
        private const val MAX_SIZE = 1024 * 1024 * 1024L //1G
        private val CACHE_PATH = File(YiApp.getApp().externalCache(), ".glide").absolutePath
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDiskCache(DiskLruCacheFactory(CACHE_PATH, MAX_SIZE))
    }

    override fun isManifestParsingEnabled(): Boolean = false
}