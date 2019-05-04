package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Query
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post

interface PostsRepository {
    @Query("Select * from posts LIMIT 100")
    fun getTop():List<Post>
}