package pl.zhp.natropie.api.responses

import org.parceler.ParcelConstructor
import pl.zhp.natropie.db.entities.Post
import java.util.*

class PostResponse @ParcelConstructor constructor(
    id: Long,
    title: String,
    content: String,
    excerpt: String,
    date: Long,
    slug: String,
    author: String,
    author_description: String,
    category: String,
    category_id: Int
) : Post(id, title, content, excerpt, date, slug, author, author_description, category, category_id) {
}