package pl.zhp.natropie.clipboard

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.*
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.ClipboardItem
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.repositories.ClipboardItemsRepository

class ClipboardManager(val db: NaTropieDB) {
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private val repository: ClipboardItemsRepository = db.clipboardItemsRepository()
    fun add(post: Post) {
        scope.launch {
            val job = GlobalScope.async {
                val item = ClipboardItem(null, post.id)
                repository.insert(item)
            }
            job.await()
        }
    }

    fun exists(post: Post, callback: ((count: Int) -> Unit)) {
        scope.launch {
            val job = GlobalScope.async {
                repository.exists(post.id)
            }
            val count = job.await()
            callback(count)
        }
    }

    fun remove(currentPost: Post) {
        scope.launch {
            val job = GlobalScope.async {
                repository.removeByPostId(currentPost.id)
            }
            job.await()
        }
    }
}