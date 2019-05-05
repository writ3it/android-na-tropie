package pl.zhp.natropie.api

import pl.zhp.natropie.api.responses.PostResponse
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post
import retrofit2.Call
import retrofit2.http.GET

interface PostsService {
    @GET("posts/?per_page=100")
    fun getFromMainPage(): Call<List<PostResponse>>
}