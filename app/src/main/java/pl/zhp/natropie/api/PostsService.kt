package pl.zhp.natropie.api

import retrofit2.Call
import retrofit2.http.GET

interface PostsService {
    @GET("posts/?per_page=100")
    fun getFromMainPage(): Call<List<PostResponse>>
}