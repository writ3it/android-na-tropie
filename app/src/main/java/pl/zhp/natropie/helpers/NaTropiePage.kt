package pl.zhp.natropie.helpers

import android.util.Log
import pl.zhp.natropie.db.types.DateConverter

class NaTropiePage(var content: String) {

    private lateinit var title: String

    fun setTitle(title: String): NaTropiePage {
        this.title = title
        return this
    }

    private lateinit var author: String

    fun setAuthor(author: String): NaTropiePage {
        this.author = author
        return this
    }

    private lateinit var date: String

    fun setDate(date: Long): NaTropiePage {
        this.date = DateConverter.fromTimestamp(date)
        return this
    }

    private lateinit var category: String

    fun setCategory(category: String): NaTropiePage {
        this.category = category
        return this
    }

    fun getHtml():String{
        val data= "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"/></head>" +
                "<body><div id=\"content\"><h2 class=\"title\">$title</h2>"+
                "<p class=\"meta\"><span class=\"dzial\">$category</span>/<span class=\"autor\">$author</span>/<span class=\"time\">$date</span></p>"+
                content+"</div></body></html>"
        Log.i(">>>>>>>>>>>>",data)
        return data
    }

}
