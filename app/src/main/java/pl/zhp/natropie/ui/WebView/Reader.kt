package pl.zhp.natropie.ui.WebView

import android.app.Activity
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.*
import org.parceler.Parcels
import pl.zhp.natropie.ReaderActivity
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.helpers.NaTropiePage
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.ui.models.PostVM


/**
 * TODO: refactor! This isn't toooo much solid
 */
class Reader(val context: Context, private val activity: AppCompatActivity) : WebViewClient() {
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private val table = NaTropieDB.getInstance(context)?.postsRepository()!!

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (!url.startsWith(READER_PREFIX)) {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(browserIntent)
            return true
        }
        reloadActivity(view, url)
        return true
    }

    private fun reloadActivity(view: WebView, url: String) {
        val slug: String = getSlugFromUrl(url)

        scope.launch {
            val job = GlobalScope.async {
                Track.DisplayList(Track.MAINPAGE)
                table.getBySlug(slug)
            }
            val post: PostWithColor? = job.await()
            if (post !== null) {
                loadPage(view, post)
            } else {
                Toast.makeText(context, "Nie znaleziono artykułu", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadPage(webView: WebView, post: PostWithColor) {
        val doc = NaTropiePage(post.content)
        doc.setTitle(post.title)
        if (post.category != "PAGES") {
            doc.setAuthor(post.author)
                .setCategory(post.category)
                .setDate(post.date)
        }
        Track.DisplayPost(post.id, post.title, post.author, post.category)
        webView.loadDataWithBaseURL("file:///android_asset/", doc.getHtml(), "text/html", "UTF-8", "")
    }

    private fun getSlugFromUrl(url: String): String {
        return url.replace(READER_PREFIX, "").replace("/", "")
    }

    companion object {
        const val READER_PREFIX = "natropie://"
    }

}