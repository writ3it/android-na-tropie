package pl.zhp.natropie.api

import pl.zhp.natropie.db.entities.Category
import retrofit2.Call
import retrofit2.http.GET

class PostsService {
    @GET("posts/?per_page=100")
    fun getFromMainPage(): Call<List<Post>>
}