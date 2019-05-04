package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
@Entity(tableName="posts")
data class Post @ParcelConstructor constructor(
    @PrimaryKey(autoGenerate=false)
    var id:Int,
    @ColumnInfo(name="date", typeAffinity = ColumnInfo.da)
    var date:String,
    @ColumnInfo(name="url")
    var link:String,
    @ColumnInfo(name="title")
    var title:String,
    @ColumnInfo(name="content_rendered", typeAffinity = ColumnInfo.TEXT)
    var content:String,
    @ColumnInfo(name="excerpt_rendered", typeAffinity = ColumnInfo.TEXT)
    var excerpt:String,
    var author:Int,
    var categories:List<Int>,
    var featured_media:Int,
    var status:String
):AEntity {
}