package pl.zhp.natropie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.app_bar_reader.*

class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
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
