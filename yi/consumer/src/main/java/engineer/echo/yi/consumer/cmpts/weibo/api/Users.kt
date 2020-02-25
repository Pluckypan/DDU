package engineer.echo.yi.consumer.cmpts.weibo.api

import androidx.lifecycle.LiveData
import engineer.echo.yi.consumer.cmpts.weibo.Weibo
import engineer.echo.yi.consumer.cmpts.weibo.bean.Account
import engineer.echo.yi.consumer.cmpts.weibo.bean.UserList
import retrofit2.http.GET
import retrofit2.http.Query

interface Users {

    @GET(Weibo.API_URL.plus("users/show.json"))
    fun getUser(@Query("uid") uid: String): LiveData<Account>

    @GET(Weibo.API_URL.plus("friendships/friends.json"))
    fun getFollowing(@Query("uid") uid: String, @Query("count") count: Int = 5, @Query("cursor") cursor: Int = 0): LiveData<UserList>
}