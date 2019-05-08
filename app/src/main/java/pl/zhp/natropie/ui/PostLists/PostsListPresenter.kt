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
import pl.zhp.natropie.ui.models.PostVM
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
                if (categoryId == 0) {
                    table.getForMainPage()
                } else {
                    table.getFor(categoryId)
                }
            }
            val data = job.await()
            adapter?.showPostsList(data.map { PostVM(it) })
        }
    }

    private var adapter: PostsListPresenter.UpdatetableAdapter? = null

    fun attachToAdapter(_adapter: UpdatetableAdapter?) {
        adapter = _adapter
    }

    private var categoryId: Int

    fun setCategoryId(selectedCategoryId: Int) {
        categoryId = selectedCategoryId
    }

    interface UpdatetableAdapter{
        fun showPostsList(data:List<PostVM>)
    }
}