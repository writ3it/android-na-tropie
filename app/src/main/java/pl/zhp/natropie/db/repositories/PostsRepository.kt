package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import pl.zhp.natropie.db.entities.Post

@Dao
interface PostsRepository {
    @Query("Select * from posts LIMIT 100")
    fun getTop():List<Post>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(data:Post)
}