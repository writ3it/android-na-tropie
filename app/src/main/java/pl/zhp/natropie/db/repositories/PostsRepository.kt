package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.database.Cursor
import pl.zhp.natropie.db.entities.Post

@Dao
interface PostsRepository {
    @Query("Select * from posts LIMIT 100")
    fun getTop():List<Post>

    @Query("Select * from posts ORDER BY date DESC LIMIT 100")
    fun getForMainPage():List<Post>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(data:Post)

    @Query("Select * from posts WHERE category_id = :categoryId ORDER BY date DESC LIMIT 100")
    fun getFor(categoryId: Int): List<Post>
}