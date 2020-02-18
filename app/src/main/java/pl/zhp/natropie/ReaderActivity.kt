package pl.zhp.natropie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_reader.*
import kotlinx.android.synthetic.main.content_reader.*
import org.parceler.Parcels
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.ui.WebView.Reader
import pl.zhp.natropie.clipboard.ClipboardManager
import pl.zhp.natropie.db.NaTropieDB


open class ReaderActivity : AppCompatActivity() {

    private var menu: Menu? = null
    private var currentPost: PostWithColor? = null
    private lateinit var reader: Reader
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
        clipboardManager = ClipboardManager(NaTropieDB.getInstance(applicationContext)!!)
        reader = Reader(applicationContext, this)
        reader.onPostChanged = fun(post: PostWithColor): Unit {
            this.postUpdated(post)
        }
        initView(web_content)

    }

    private fun postUpdated(post: PostWithColor) {
        currentPost = post
        initClipboard()
    }

    private fun initClipboard() {
        clipboardManager.exists(currentPost!!, fun(count: Int) {
            val menuItem = toolbar.menu.findItem(R.id.clipboard) ?: return
            if (count > 0) {
                setBookmarked(menuItem)
            } else {
                setNotBookmarked(menuItem)
            }
        })
    }

    private fun initView(webContent: WebView) {
        var post: PostWithColor = intent.getParcelableExtra<Parcelable>(VAR_POST)
            .let { Parcels.unwrap<PostWithColor>(it) }
        currentPost = post
        webContent.webViewClient = reader
        webContent.settings.allowUniversalAccessFromFileURLs = true
        webContent.settings.javaScriptEnabled = true
        reader.loadPage(webContent, post)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_reader_drawer, menu)
        initClipboard()
        return true
    }

    private fun setNotBookmarked(item: MenuItem) {
        item.icon = getDrawable(R.drawable.ic_bookmark_border_black_24dp)
    }

    private fun setBookmarked(item: MenuItem) {
        item.icon = getDrawable(R.drawable.ic_bookmark_black_24dp)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_back) {
            finish()
            return true
        }
        if (item?.itemId == R.id.share) {
            share()
        }
        if (item?.itemId == R.id.clipboard) {
            addToClipboard(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToClipboard(item: MenuItem) {
        clipboardManager.exists(currentPost!!, fun(count: Int) {
            if (count == 0) {
                clipboardManager.add(currentPost!!)
                setBookmarked(item)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.NT_CLIPBOARD_ADDED),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                clipboardManager.remove(currentPost!!)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.NT_CLIPBOARD_REMOVED),
                    Toast.LENGTH_SHORT
                )
                    .show()
                setNotBookmarked(item)
            }
        })
    }

    private fun share() {
        if (currentPost == null) {
            return
        }
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, currentPost!!.title)
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.NT_SHARE_PATTERN).format(currentPost!!.title, currentPost!!.slug)
        )
        startActivity(Intent.createChooser(sharingIntent, "Udostępnij przez"))
    }

    fun etGoHome(view: View) {
        finish()
    }

    companion object {
        const val VAR_POST = "pl.zhp.natropie.ReaderActivity.VAR_POST"
    }
}
