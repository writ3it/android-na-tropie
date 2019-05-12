package pl.zhp.natropie.ui.models

import android.arch.lifecycle.ViewModel
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.db.types.DateConverter

class PostVM(post:PostWithColor) : ViewModel() {
    var category = post.category
    var title = post.title
    var color = post.color
    var excerpt = post.excerpt
    var author_and_date = post.author+" / "+ DateConverter.fromTimestamp(post.date)
    var id = post.id
    var Model:PostWithColor = post
}