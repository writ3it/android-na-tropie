package pl.zhp.natropie.ui.PostLists

import android.content.Context
import android.database.Cursor
import android.provider.Contacts
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import kotlinx.coroutines.*
import pl.zhp.natropie.R
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.repositories.PostsRepository
import java.lang.Exception


class PostsListPresenter(val context: Context?, val table:PostsRepository){
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun detachView() {
        job.cancel()
    }

    fun refresh(){
        scope.launch {
            val job = GlobalScope.async {
                table.getForMainPage()
            }
            val data = job.await()
            adapter?.showPostsList(data)
        }
    }

    private var adapter: PostsListPresenter.UpdatetableAdapter? = null

    fun attachToAdapter(_adapter: UpdatetableAdapter?) {
        adapter = _adapter
    }

    interface UpdatetableAdapter{
        fun showPostsList(data:List<Post>)
    }
}