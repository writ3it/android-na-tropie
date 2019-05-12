package pl.zhp.natropie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.app_bar_reader.*
import kotlinx.android.synthetic.main.content_reader.*
import org.parceler.Parcels
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.db.types.DateConverter
import pl.zhp.natropie.ui.NaTropiePage

class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
        initView();
    }

    private fun initView() {
        val post =intent.getParcelableExtra<Parcelable>(VAR_POST).let { Parcels.unwrap<PostWithColor>(it) }
        val doc = NaTropiePage(post.content)
        doc.setTitle(post.title)
            .setAuthor(post.author)
            .setDate(post.date)
            .setCategory(post.category)
        web_content.loadDataWithBaseURL("",doc.getHtml(),"text/html","UTF-8","")
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

    companion object {
        const val VAR_POST = "pl.zhp.natropie.ReaderActivity.VAR_POST"
    }
}
