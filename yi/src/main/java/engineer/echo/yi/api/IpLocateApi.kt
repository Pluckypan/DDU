package engineer.echo.yi.api

import androidx.lifecycle.LiveData
import engineer.echo.yi.bean.location.IpLocation
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface IpLocateApi {

    companion object {
        const val API_URL = "http://ip-api.com/json/"
    }

    @FormUrlEncoded
    @POST(API_URL)
    fun getLocation(
        @Field("app") app: String = "EasyApi",
        @Query("lang") lang: String = "zh-CN"
    ): LiveData<IpLocation>
}