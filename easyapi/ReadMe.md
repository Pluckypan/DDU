# EasyApi

LiveData EveryWhere...

## 背景

Android 开发过程中,难免遇到内存泄露的问题.`Google` 的原则是尽量避免在界面处于不可见状态下更新界面,为了解决这个问题,`Google` 在 `Architecture` 中引入了 `lifecycle` 的概念,在 `lifecycle` 中借助 `LiveData` 我们可以轻松的实现`可被订阅` & `生命周期感知` 的数据源

当前,基于数据驱动UI的模式被越来越多的开发人员所接受， `MVVM` 架构应运而生.为了让 `MVVM` & `LiveData` 执行的更加彻底, **`EasyApi`** 诞生了,就让我们一起体验架构之美吧。 

## 简介

**`EasyApi`** 主要结合了 `LiveData` & `Retrofit`,支持接口请求,支持文件下载,支持后台作业

## 功能特点
- [x] 接口请求直接返回 LiveData
- [x] 支持文件下载,下载状态通过 LiveData 下发
- [x] 接口请求支持取消
- [x] 文件下载支持断点续传
- [x] 文件下载属于全局状态,可在不同界面订阅
- [x] 可灵活配置自己的 `Retrofit` 和 `Client`
- [x] 有全局监听器,方便观察执行结果、耗时、流量大小.针对错误可做统一处理
- [x] 接口友好,使用简单
- [x] 全面的日志打印,通过关键字 `EasyApi` 可以很方便查看请求情况
- [x] `EasyApi` 所有的请求(包括下载)都有id,通过id均可取消
- [x] 支持后台作业 `EasyJob` (耗时操作,如文件解压,数据库操作)
- [x] `EasyJob` 完全复用 `Retrofit` 的线程池，线程管理方便
- [x] 支持模块化(动态代理+接口下沉)：`EasyProxy`

## TODO
- [ ] 后台作业 `EasyJob` 支持进度回调 ProgressResult
- [ ] 取消「后台作业」
- [ ] 「后台作业」 `EasyJob` 支持复杂入参和复杂返回类型
- [ ] `Release` 环境混淆检测,依赖检测,性能数据

## 简单示例

### 接口请求

``` kotlin
// 定义接口
interface IpLocateApi {

    companion object {
        private const val API_URL = "http://ip-api.com/json/"
    }

    @FormUrlEncoded
    @POST(API_URL)
    fun getLocation(
        @Field("app") app: String = "EasyApi",
        @Query("lang") lang: String = "zh-CN"
    ): LiveData<IpLocation>
}

// 请求接口
override val locationData: LiveData<IpLocation> =
        EasyApi.create(IpLocateApi::class.java).getLocation()
```

### 文件下载

``` kotlin
// 直接订阅
EasyApi.download(APK_URL,"sdcard/xxx/xxx/xxx.apk").observe(owner) {
    println(it)            
}

// 下载触发器
private val downloadTrigger = MutableLiveData<Boolean>()

// 下载
override val downloadData: LiveData<DownloadState> =
    Transformations.switchMap(downloadTrigger) { apk ->
        model.download(apk)
    }

// 触发下载
override fun startDownload(apk: Boolean) {
    downloadTrigger.value = apk
}

// 取消下载「方式一」
downloadData.cancelRequest()

// 取消下载「方式二」
EasyApi.cancelDownload(id)
```

### 后台作业
```
// 定义接口 注意 retrofit = true
@JobApi(uniqueId = "Zip@Producer", retrofit = true)
interface ZipApi {


    fun zip(source: String, target: String, i: Int, b: Boolean, f: Float, l: Long): Result

    fun unzip(source: String, target: String): Result
    // 如果是带进度回调的 最后一个参数需要是 JobCallback，并且返回类型需要继承自 ProgressResult
    fun unzipProgress(source: String, target: String, listener: JobCallback): ZipState
}

// 定义服务
@JobServer(uniqueId = "Zip@Producer")
class ZipServer : ZipApi {

    override fun zip(
        source: String,
        target: String,
        i: Int,
        b: Boolean,
        f: Float,
        l: Long
    ): Result {
        return Result(if (File(target).zip(source)) null else Exception("zip failed"))
    }

    override fun unzip(source: String, target: String): Result {
        return Result(if (ZipFile(source).unZipTo(target)) null else Exception("unzip failed"))
    }

    override fun unzipProgress(
        source: String,
        target: String,
        listener: JobCallback
    ): ZipState {
        return ZipState().apply {
            exception =
                if (ZipFile(source).unZipTo(target) { total, index, progress ->
                        listener.onJobState(State.OnProgress, total, index, progress)
                    }) null else Exception("unzip progress failed")
            msg = if (exception == null) "success!kind of~" else "failed omg~~~"
        }
    }
}

// 调用服务 注意传入的 Interface 为 ZipApiRetrofit 而不是 ZipApi
EasyApi.create(ZipApiRetrofit::class.java).unzip(source, target)
```

**后台作业** 在 `EasyApi` 中叫做 `EasyJob`。设计的思想是把本地的耗时操作抽象为服务端的接口,和处理服务端的接口一样:发起请求 `Request` + 得到响应 `Response`。我们需要做的很简单：
1. 定义接口如 `ZipApi`, 加上注解 `@JobApi(uniqueId = "Zip@Producer", retrofit = true)`
2. 实现接口 `class ZipServer : ZipApi` 加上注解 `@JobServer(uniqueId = "Zip@Producer")`
3. 调用 `EasyApi.create(ZipApiRetrofit::class.java).unzip(source, target)`
4. 需要注意的是,和请求服务端接口一样,数据返回类型需要继承自 `Result`
5. 带进度的后台作业需要注意两点：最后一个参数指定 `JobCallback` ,返回类型继承自 `ProgressResult`

### 实现原理
```
// easyapi.compiler 生成的接口信息
public interface ZipApiRetrofit {
  @GET("EasyApi/EasyProxy/?_api_=engineer.echo.yi.producer.cmpts.zip.ZipApi&_method_=zip")
  @Headers({
      "source:java.lang.String",
      "target:java.lang.String",
      "i:int",
      "b:boolean",
      "f:float",
      "l:long"
  })
  LiveData<Result> zip(@Query("source") String source, @Query("target") String target,
      @Query("i") int i, @Query("b") boolean b, @Query("f") float f, @Query("l") long l);

  @GET("EasyApi/EasyProxy/?_api_=engineer.echo.yi.producer.cmpts.zip.ZipApi&_method_=unzip")
  @Headers({
      "source:java.lang.String",
      "target:java.lang.String"
  })
  LiveData<Result> unzip(@Query("source") String source, @Query("target") String target);

  @GET("EasyApi/EasyProxy/?_api_=engineer.echo.yi.producer.cmpts.zip.ZipApi&_method_=unzipProgress")
  @Headers({
      "source:java.lang.String",
      "target:java.lang.String",
      "listener:engineer.echo.easyapi.annotation.JobCallback"
  })
  LiveData<ZipState> unzipProgress(@Query("source") String source, @Query("target") String target);
}
```
`EasyJob` 的实现主要靠 `easyapi.compiler` 通过注解 `JobApi` (retrofit = true 时) 生成如上代码。可以看出，`ZipApiRetrofit` 的格式完全就是 `Retrofit` 接口定义的形式。当请求发生时,通过 `JobInterceptor` 对请求进行拦截解析,然后通过动态代理的方式 `EasyProxy` 去执行 `ZipServer` 中定义好的方法。


## Transformations

### switchMap
适用于分步执行任务,比如先通过ip定位得到 `位置` ,再通过 `位置` 请求天气接口获取 `当前天气`

``` kotlin
// #1 刷新触发器
private val refreshTrigger = MutableLiveData<Boolean>()

// #2 Ip定位
override val locationData: LiveData<IpLocation> = Transformations.switchMap(refreshTrigger) {
    if (it == true) {
        EasyApi.create(IpLocateApi::class.java).getLocation()
    } else {
        null
    }
}

// #3 获取天气
override val weatherData: LiveData<WeatherResp> =
    Transformations.switchMap(locationData) {
        if (it != null) {
            EasyApi.create(WeatherApi::class.java).getWeather(location = it.getQueryLocation())
        } else {
            null
        }
    }

// #0 触发刷新
override fun refresh() {
    refreshTrigger.value = true
}
```
上面代码的触发流程是: `refresh()`触发刷新 -> `触发器为 true` -> `触发ip定位` -> `触发获取天气`。可以看到 **LiveData 的世界里执行一个事件的根源仍然是一个 LiveData**

### map
适用于数据类型转换,比如ip定位得到 `位置` 后,需要对数据进行深加工

``` kotlin
// ip 定位数据
override val locationData: LiveData<IpLocation> =
    EasyApi.create(IpLocateApi::class.java).getLocation()

// #4 深加工数据
override val titleData: LiveData<String> = Transformations.map(locationData) {
    it.city
}
```

### 原理
switchMap 和 map 内部都是通过 `MediatorLiveData` 来实现的,`MediatorLiveData`

```

@MainThread
public static <X, Y> LiveData<Y> map(
        @NonNull LiveData<X> source,
        @NonNull final Function<X, Y> mapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(source, new Observer<X>() {
        @Override
        public void onChanged(@Nullable X x) {
            result.setValue(mapFunction.apply(x));
        }
    });
    return result;
}

@MainThread
public static <X, Y> LiveData<Y> switchMap(
        @NonNull LiveData<X> source,
        @NonNull final Function<X, LiveData<Y>> switchMapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(source, new Observer<X>() {
        LiveData<Y> mSource;

        @Override
        public void onChanged(@Nullable X x) {
            LiveData<Y> newLiveData = switchMapFunction.apply(x);
            if (mSource == newLiveData) {
                return;
            }
            if (mSource != null) {
                result.removeSource(mSource);
            }
            mSource = newLiveData;
            if (mSource != null) {
                result.addSource(mSource, new Observer<Y>() {
                    @Override
                    public void onChanged(@Nullable Y y) {
                        result.setValue(y);
                    }
                });
            }
        }
    });
    return result;
}
```

## 动态代理
```
// 定义接口
@JobApi(uniqueId = "AAA")
interface ProxyApi {

    fun add(a: Int, b: Int): Int

    fun showToast(msg: String)

    fun print():String

    fun go()
}

// 定义接口实现
@JobServer(uniqueId = "AAA")
class ProxyServer : ProxyApi {

    override fun add(a: Int, b: Int): Int = a + b

    override fun showToast(msg: String) = msg.toastLong(EasyApp.getApp())

    override fun print(): String {
        return "EasyApi ${add(1, 3)}"
    }

    override fun go() {
        showToast(print())
    }
}

// 调用
EasyApi.getProxy(ProxyApi::class.java).also {
    it.showToast("${it.add(3,9)}")
    it.print()
    it.go()
}
```

`EasyProxy` 的实现原理是动态代理,主要用到 `InvocationHandler` & `Proxy` 。`EasyProxy` 的应用场景：模块化(接口下沉的方式)、`EasyJob` 等等


## 实现原理
- LiveData 的实现：`addCallAdapterFactory(LiveDataCallAdapterFactory.create(monitor))` Retrofit 支持自定义返回类型,可参考官方 RxJava 的实现
- 下载的实现：下载和接口请求大同小异,反射得到类型为下载任务后进行下载处理
- `EasyJob`后台作业的实现：`JobInterceptor` ,`AutoService` & 注解 & `JavaPoet` , 反射，`EasyProxy` 动态代理
- 模块化：`EasyProxy` 动态代理 & 接口下沉

## 文件结构

### easyapi.annotation
```
.
└── engineer
    └── echo
        └── easyapi
            └── annotation
                ├── EasyJobHelper.java
                ├── JobApi.java
                ├── JobServer.java
                └── MD5Tool.java

4 directories, 4 files

```

### easyapi.compiler
```
.
└── engineer
    └── echo
        └── easyapi
            └── compiler
                ├── CompilerHelper.java
                └── EasyProcessor.java

4 directories, 2 files
```

### EasyApi
```
└── engineer
    └── echo
        └── easyapi
            ├── EasyApi.kt
            ├── EasyLiveData.kt
            ├── EasyMonitor.kt
            ├── LiveDataCallAdapterFactory.kt
            ├── ProgressResult.kt
            ├── Result.kt
            ├── State.kt
            ├── api
            │   ├── ApiHelper.kt
            │   └── LiveDataApiCallAdapter.kt
            ├── download
            │   ├── DownloadApi.kt
            │   ├── DownloadHelper.kt
            │   ├── DownloadState.kt
            │   └── LiveDataDownloadAdapter.kt
            ├── job
            │   ├── JobHelper.kt
            │   ├── JobInterceptor.kt
            │   └── NetInterceptor.kt
            ├── proxy
            │   ├── DefaultHandler.kt
            │   ├── EasyHandler.kt
            │   └── EasyProxy.kt
            └── pub
                ├── Extendx.kt
                └── MD5Tool.kt

8 directories, 21 files
```

## 示例 demo
[MVVM & 模块化请戳](../yi/ReadMe.md)

## 参考文档
- [retrofit](https://github.com/square/retrofit)
- [javaPoet](https://github.com/square/javapoet)
- [Wandroid](https://github.com/iamyours/Wandroid)