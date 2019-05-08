package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import org.parceler.Parcel
import org.parceler.ParcelConstructor
import pl.zhp.natropie.db.types.CategoryIdsConverter
import pl.zhp.natropie.db.types.DateConverter
import java.util.*

@Parcel(Parcel.Serialization.BEAN)
@Entity(tableName="posts")
open class Post @ParcelConstructor constructor(
    @PrimaryKey(autoGenerate=false)
    var id:Long,
    @ColumnInfo(name="title")
    var title:String,
    @ColumnInfo(name="content_html", typeAffinity = ColumnInfo.TEXT)
    var content:String,
    @ColumnInfo(name="excerpt", typeAffinity = ColumnInfo.TEXT)
    var excerpt:String,
    @ColumnInfo(name="date")
    var date: Long,
    @ColumnInfo(name="slug")
    var slug:String,
    @ColumnInfo(name="author")
    var author:String,
    @ColumnInfo(name="author_description")
    var author_description:String,
    @ColumnInfo(name="category")
    var category:String,
    @ColumnInfo(name="category_id")
    var category_id:Int
):AEntity {
}