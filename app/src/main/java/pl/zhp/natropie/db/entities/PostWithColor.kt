package pl.zhp.natropie.db.entities

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
class PostWithColor @ParcelConstructor constructor(
    id: Long,
    title: String,
    content: String,
    excerpt: String,
    date: Long,
    slug: String,
    author: String,
    author_description: String,
    category: String,
    category_id: Int,
    val color:String
) : Post(id, title, content, excerpt, date, slug, author, author_description, category, category_id) {

}
