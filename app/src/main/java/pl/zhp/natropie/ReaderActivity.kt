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

open class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
        initView(web_content)
    }

    private fun initView(webContent:WebView) {
        var post:PostWithColor = intent.getParcelableExtra<Parcelable>(VAR_POST).let { Parcels.unwrap<PostWithColor>(it) }

        val doc = NaTropiePage(post.content)
        doc.setTitle(post.title)
        if(post.category!="PAGES") {
            doc.setAuthor(post.author)
                .setCategory(post.category)
                .setDate(post.date)
        }
        Track.DisplayPost(post.id, post.title,post.author,post.category)
        webContent.settings.allowUniversalAccessFromFileURLs = true
        webContent.settings.javaScriptEnabled = true
        webContent.webViewClient = Reader(applicationContext,this)
        webContent.loadDataWithBaseURL("file:///android_asset/",doc.getHtml(),"text/html","UTF-8","")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_reader_drawer,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_back){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun test(view: View){
        finish()
    }

    fun etGoHome(view:View){
        finish()
    }

    companion object {
        const val VAR_POST = "pl.zhp.natropie.ReaderActivity.VAR_POST"
    }
}
