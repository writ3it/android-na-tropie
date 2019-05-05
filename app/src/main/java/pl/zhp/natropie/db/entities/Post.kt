package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import org.parceler.Parcel
import org.parceler.ParcelConstructor
import pl.zhp.natropie.db.types.CategoryIdsConverter
import java.util.*

@Parcel(Parcel.Serialization.BEAN)
@Entity(tableName="posts")
data class Post @ParcelConstructor constructor(
    @PrimaryKey(autoGenerate=false)
    var id:Int,
    @ColumnInfo(name="date")
    var date: Date,
    @ColumnInfo(name="url")
    var link:String,
    @ColumnInfo(name="title")
    var title:String,
    @ColumnInfo(name="content_rendered", typeAffinity = ColumnInfo.TEXT)
    var content:String,
    @ColumnInfo(name="excerpt_rendered", typeAffinity = ColumnInfo.TEXT)
    var excerpt:String,
    @ColumnInfo(name="author")
    var author:Int,
    @ColumnInfo(name="categories")
    var categories:List<Int>,
    @ColumnInfo(name="featured_media")
    var featured_media:Int,
    @ColumnInfo(name="status")
    var status:String
):AEntity {
}