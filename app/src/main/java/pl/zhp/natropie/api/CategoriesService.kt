package pl.zhp.natropie.api

import pl.zhp.natropie.api.responses.CategoryResponse
import pl.zhp.natropie.db.entities.Category
import retrofit2.Call
import retrofit2.http.GET

interface CategoriesService {
    @GET("categories/?per_page=100")
    fun listCategories(): Call<List<Category>>
}