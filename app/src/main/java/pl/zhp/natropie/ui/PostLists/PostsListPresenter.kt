package pl.zhp.natropie.ui.PostLists

import android.content.Context
import android.database.Cursor
import android.provider.Contacts
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import kotlinx.coroutines.*
import pl.zhp.natropie.R
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.repositories.PostsRepository
import pl.zhp.natropie.tracking.Track
import pl.zhp.natropie.ui.models.PostVM
import java.lang.Exception


class PostsListPresenter(val context: Context?, val table: PostsRepository) {
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun detachView() {
        job.cancel()
    }

    fun refresh() {
        scope.launch {
            val job = GlobalScope.async {
                Log.i("@PostsListPresenter", "Display from list = $categoryId")
                when (categoryId) {
                    CLIPBOARD -> {
                        Track.DisplayList(Track.CLIPBOARD)
                        table.getFromClipboard()
                    }
                    MAINPAGE -> {
                        Track.DisplayList(Track.MAINPAGE)
                        table.getForMainPage()
                    }
                    else -> {
                        val result = table.getFor(categoryId)
                        Track.DisplayList(result.first().category)
                        result
                    }
                }
            }
            val data = job.await()
            Log.i("@PostsListPresenter", "Posts to display = " + data.size.toString())
            adapter?.showPostsList(data.map { PostVM(it) })
        }
    }

    private var adapter: PostsListPresenter.UpdatetableAdapter? = null

    fun attachToAdapter(_adapter: UpdatetableAdapter?) {
        adapter = _adapter
    }

    private var categoryId: Int = 0

    fun setCategoryId(selectedCategoryId: Int) {
        categoryId = selectedCategoryId
    }

    fun setAsClipboard() {
        categoryId = CLIPBOARD
    }

    companion object {
        private const val CLIPBOARD = -1
        private const val MAINPAGE = 0
    }

    interface UpdatetableAdapter {
        fun showPostsList(data: List<PostVM>)
    }
}