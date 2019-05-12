package pl.zhp.natropie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

    }

    override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    companion object {
        const val VAR_POST = "pl.zhp.natropie.ReaderActivity.VAR_POST"
    }
}
