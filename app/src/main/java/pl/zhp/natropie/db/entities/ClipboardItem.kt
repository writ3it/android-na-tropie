package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
@Entity(
    tableName = "clipboard_items",
    foreignKeys = [ForeignKey(
        entity = Post::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("post_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
open class ClipboardItem @ParcelConstructor constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "post_id")
    var postId: Long
) {

}