package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import pl.zhp.natropie.db.entities.ClipboardItem
import pl.zhp.natropie.db.entities.Post

@Dao
interface ClipboardItemsRepository {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ClipboardItem)

    @Query("SELECT count(*) FROM clipboard_items ci WHERE ci.post_id=:postId")
    fun exists(postId: Long): Int

    @Query("DELETE FROM clipboard_items WHERE post_id=:postId")
    fun removeByPostId(postId: Long)
}