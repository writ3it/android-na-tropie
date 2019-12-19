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
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.ui.models.PostVM


/**
 * TODO: refactor! This isn't toooo much solid
 */
class Reader(val context: Context, val activity: AppCompatActivity) : WebViewClient() {
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private val table = NaTropieDB.getInstance(context)?.postsRepository()!!

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (!url.startsWith(READER_PREFIX)) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
            return true
        }
        reloadActivity(activity, url)
        return true
    }

    private fun reloadActivity(activity: AppCompatActivity, url: String) {
        activity.finish()
        val slug: String = getSlugFromUrl(url)

        scope.launch {
            val job = GlobalScope.async {
                Track.DisplayList(Track.MAINPAGE)
                table.getBySlug(slug)
            }
            val post: PostWithColor? = job.await()

            val intent = Intent(activity, ReaderActivity::class.java)
            intent.putExtra(ReaderActivity.VAR_POST, Parcels.wrap(post))

            activity.startActivity(intent)
        }

    }

    private fun getSlugFromUrl(url: String): String {
        return url.replace(READER_PREFIX, "").replace("/", "")
    }

    companion object {
        const val READER_PREFIX = "natropie://"
    }

}