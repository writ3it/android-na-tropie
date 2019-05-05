package pl.zhp.natropie.api.responses

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import org.parceler.Parcel
import org.parceler.ParcelConstructor
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.types.DateConverter
import pl.zhp.natropie.helpers.Text
import java.util.*
import kotlin.collections.HashMap

@Parcel(Parcel.Serialization.BEAN)
data class PostResponse @ParcelConstructor constructor(
    var id:Int,
    var date: Date,
    var link:String,
    var title:HashMap<String,String>,
    var content:HashMap<String,String>,
    var excerpt:HashMap<String,String>,
    var author:Int,
    var categories:List<Int>,
    var featured_media:Int,
    var status:String
){
    fun toPost():Post{
        val cnv = DateConverter()
        return Post(id,date,link,
            title["rendered"]!!,
            content["rendered"]!!,
            Text.stripHtml(excerpt["rendered"]!!),
            author,
            categories,
            featured_media,
            status)
    }
}