package pl.zhp.natropie.api

import pl.zhp.natropie.api.responses.PostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("posts/newest/{timestamp}")
    fun getFromMainPage(@Path("timestamp") timestamp:Long): Call<List<PostResponse>>

    @GET("posts/get-about-us")
    fun getAboutUs():Call<PostResponse>
    @GET("posts/privacy")
    fun getPrivacy():Call<PostResponse>
}