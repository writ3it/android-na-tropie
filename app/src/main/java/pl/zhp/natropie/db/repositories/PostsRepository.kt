package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.database.Cursor
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.entities.PostWithColor

@Dao
interface PostsRepository {
    @Query("Select * from posts LIMIT 100")
    fun getTop():List<Post>

    @Query("Select p.*,c.box_color as color from posts p join categories c on p.category_id = c.id  ORDER BY date DESC LIMIT 100")
    fun getForMainPage():List<PostWithColor>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(data:Post)

    @Query("Select p.*,c.box_color as color from posts p join categories c on p.category_id = c.id WHERE category_id = :categoryId ORDER BY date DESC LIMIT 100")
    fun getFor(categoryId: Int): List<PostWithColor>

    @Query("Select p.*,c.box_color as color from posts p join categories c on p.category_id = c.id  WHERE p.id=:postId")
    fun get(postId: Int): PostWithColor
}