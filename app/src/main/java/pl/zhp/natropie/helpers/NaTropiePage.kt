package pl.zhp.natropie.helpers

import android.util.Log
import pl.zhp.natropie.db.types.DateConverter

class NaTropiePage(var content: String) {

    private lateinit var title: String

    fun setTitle(title: String): NaTropiePage {
        this.title = title
        return this
    }

    private var author: String? = null

    fun setAuthor(author: String): NaTropiePage {
        this.author = author
        return this
    }

    private var date: String? = null

    fun setDate(date: Long): NaTropiePage {
        this.date = DateConverter.fromTimestamp(date)
        return this
    }

    private var category: String? = null

    fun setCategory(category: String): NaTropiePage {
        this.category = category
        return this
    }

    private val imageSizeRegex = Regex("(width=\\\"[^\"]+\\\"\\s+height=\\\"[^\"]+\\\"|height=\\\"[^\"]+\\\"\\s+width=\\\"[^\"]+\\\")")

    fun getHtml():String{
        val data= "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"/></head>" +
                "<body><div id=\"content\"><h2 class=\"title\">$title</h2>"+
                "<p class=\"meta\">" +
        if(category!=null) {
            "<span class=\"dzial\">$category</span>/"
        } else { "" } +
        if (author!=null){
            "<span class=\"autor\">$author</span>/"
        } else {
            ""
        } +
                if (date!=null){
                    "<span class=\"time\">$date</span>"
                } else {
                    ""
                } +
        "</p>"+
                content+"</div></body></html>"
        return imageSizeRegex.replace(data,"")
    }

}
