package engineer.echo.yi.api

import androidx.lifecycle.LiveData
import engineer.echo.yi.bean.location.IpLocation
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IpLocateApi {

    companion object {
        private const val API_URL = "http://ip-api.com/json/"
    }

    @FormUrlEncoded
    @POST(API_URL)
    fun getLocation(@Field("app") app: String = "EasyApi"): LiveData<IpLocation>
}