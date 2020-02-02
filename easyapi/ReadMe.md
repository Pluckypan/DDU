# EasyApi

## 背景

Android 开发过程中,难免遇到内存泄露的问题.`Google` 的原则是尽量避免在界面处于不可见状态时更新界面,为了解决这个问题,`Google` 在 `Architecture` 中引入了 `lifecycle` 的概念,在 `lifecycle` 中借助 `LiveData` 我们可以轻松的实现`可被订阅` & `生命周期感知` 的数据源

当前,基于数据驱动UI这种思想的架构 `MVVM` 已被越来越多的开发人员所接受.为了让 `MVVM` & `LiveData` 执行的更加彻底,于是诞生了 **`EasyApi`** 

## 简介

**`EasyApi`** 主要结合了 `LiveData` & `Retrofit`,支持接口请求,支持文件下载,支持后台任务(实现中)

## 功能特点
- [x] 接口请求直接返回 LiveData
- [x] 支持文件下载,下载状态通过 LiveData 下发
- [x] 接口请求支持取消
- [x] 文件下载支持断点续传
- [x] 文件下载属于全局状态,可在不同界面订阅
- [x] 可灵活配置自己的 `Retrofit` 和 `Client`
- [x] 有全局监听器,方便观察执行结果、耗时、流量大小.针对错误可做统一处理
- [x] 接口友好,使用简单
- [x] 全面的日志打印,通过 `EasyApi` 可以很方便查看请求情况
- [x] `EasyApi` 所有的请求(包括下载)都有id,通过id均可取消
- [ ] **`「开发中」`** 支持后台任务(耗时操作,如文件解压,数据库操作),并支持组件化调用

## 简单示例

### 文件下载

``` kotlin
// 直接订阅
EasyApi.download(APK_URL,"sdcard/xxx/xxx/xxx.apk").observe(owner) {
    println(it)            
}

// 状态转发：将下载状态转发给 downloadData,方便结合 DataBinding 使用 
override val downloadData: MutableLiveData<DownloadState> = MutableLiveData()
// 开始下载
EasyApi.download(APK_URL,"sdcard/xxx/xxx/xxx.apk").assignTo(downloadData)

// 取消下载「方式一」
downloadData.cancelRequest()
// 取消下载「方式二」
EasyApi.cancelDownload(id)
```

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

## Transformations

### switchMap
适用于分步执行任务,比如先通过ip定位得到 `位置` ,再通过 `位置` 请求天气接口获取 `当前天气`

``` kotlin
// ip 定位数据
override val locationData: LiveData<IpLocation> =
        EasyApi.create(IpLocateApi::class.java).getLocation()

// 天气数据
override val weatherData: LiveData<WeatherResp> =
    Transformations.switchMap(locationData) {
        EasyApi.create(WeatherApi::class.java).getWeather(location = it.getQueryLocation())
    }
```

### map
适用于数据类型转换,比如ip定位得到 `位置` 后,需要对数据进行深加工

``` kotlin
// ip 定位数据
override val locationData: LiveData<IpLocation> =
    EasyApi.create(IpLocateApi::class.java).getLocation()

// 深加工数据
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

## 实现原理
- LiveData 的实现：`addCallAdapterFactory(LiveDataCallAdapterFactory.create(monitor))` Retrofit 支持自定义返回类型,可参考官方 RxJava 的实现
- 下载的实现：下载和接口请求大同小异,反射得到类型为下载任务后进行下载处理
- 后台任务的实现：注解 & `JavaPoet` & 反射

## 文件结构
```
.
└── engineer
    └── echo
        └── easyapi
            ├── EasyApi.kt
            ├── EasyLiveData.kt
            ├── EasyMonitor.kt
            ├── LiveDataCallAdapterFactory.kt
            ├── Result.kt
            ├── api
            │   ├── ApiHelper.kt
            │   └── LiveDataApiCallAdapter.kt
            ├── download
            │   ├── DownloadApi.kt
            │   ├── DownloadHelper.kt
            │   ├── DownloadState.kt
            │   ├── LiveDataDownloadAdapter.kt
            │   └── State.kt
            └── pub
                ├── Extendx.kt
                └── MD5Tool.kt

6 directories, 14 files

```

## 示例 demo
[请戳](../yi/ReadMe.md)

## 参考文档
- [retrofit](https://github.com/square/retrofit)
- [javaPoet](https://github.com/square/javapoet)