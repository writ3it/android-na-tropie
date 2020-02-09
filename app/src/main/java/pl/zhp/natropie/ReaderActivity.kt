package pl.zhp.natropie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_reader.*
import kotlinx.android.synthetic.main.content_reader.*
import org.parceler.Parcels
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.helpers.NaTropiePage
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.ui.WebView.Reader
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


open class ReaderActivity : AppCompatActivity() {

    private var currentPost: PostWithColor? = null
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
        reader = Reader(applicationContext, this)
        initView(web_content)
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_back) {
            finish()
            return true
        }
        if (item?.itemId == R.id.share) {
            share()
        }
        return super.onOptionsItemSelected(item)
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
