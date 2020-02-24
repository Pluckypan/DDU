package engineer.echo.yi.consumer.cmpts.weibo.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Geo(
    val longitude: String? = null,//经度坐标
    val latitude: String? = null,//维度坐标
    val city: String = "",//所在城市的城市代码
    val province: String = "",//所在省份的省份代码
    val city_name: String = "",//所在城市的城市名称
    val province_name: String = "",//所在省份的省份名称
    val address: String = "",//所在的实际地址，可以为空
    val pinyin: String = "",//地址的汉语拼音，不是所有情况都会返回该字段
    val more: String = ""//更多信息，不是所有情况都会返回该字段
) : Parcelable 