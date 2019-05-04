package pl.zhp.natropie.api

import pl.zhp.natropie.api.responses.CategoryResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoriesMetaService {
    @GET("categories/?per_page=100")
    fun listOfAllMetas(): Call<List<CategoryResponse>>
}