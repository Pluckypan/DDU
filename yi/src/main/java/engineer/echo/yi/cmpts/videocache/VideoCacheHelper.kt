package engineer.echo.yi.cmpts.videocache

import com.danikula.videocache.HttpProxyCacheServer
import engineer.echo.yi.common.EasyApp
import retrofit2.http.Url

object VideoCacheHelper {

    private val proxy by lazy {
        HttpProxyCacheServer.Builder(EasyApp.getApp())
            .maxCacheSize(1024 * 1024 * 1024) // 1GB
            .maxCacheFilesCount(20)
            .build()
    }

    fun getProxyUrl(@Url originUrl: String, allowCachedFileUri: Boolean = true): String =
        proxy.getProxyUrl(originUrl, allowCachedFileUri)

    fun isCached(@Url originUrl: String): Boolean = proxy.isCached(originUrl)
}