package pl.zhp.natropie.dialogs

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_privacy.*
import org.parceler.Parcels
import pl.zhp.natropie.R
import pl.zhp.natropie.ReaderActivity
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.helpers.NaTropiePage
import pl.zhp.natropie.tracking.Track

class PrivacyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        initView(web_content)
    }
    fun initView(webContent: WebView) {

        var post: PostWithColor = intent.getParcelableExtra<Parcelable>(ReaderActivity.VAR_POST).let { Parcels.unwrap<PostWithColor>(it) }

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
        webContent.loadDataWithBaseURL("file:///android_asset/",doc.getHtml(),"text/html","UTF-8","")
    }


}
