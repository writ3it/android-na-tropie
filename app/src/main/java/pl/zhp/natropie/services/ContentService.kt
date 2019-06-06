package pl.zhp.natropie.services

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.support.v4.content.LocalBroadcastManager
import com.google.gson.GsonBuilder
import pl.zhp.natropie.R
import pl.zhp.natropie.api.CategoriesService
import pl.zhp.natropie.api.PostsService
import pl.zhp.natropie.api.types.DateSerializer
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.AEntity
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.types.NothingResponse
import pl.zhp.natropie.tracking.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast


private const val ACTION_GET_MENU = "pl.zhp.natropie.ui.action.GET_MENU"
private const val ACTION_GET_POSTS = "pl.zhp.natropie.ui.action.GET_POSTS"
private const val ACTION_GET_POSTS_CATEGORY_ID = "pl.zhp.natropie.ui.action.ACTION_GET_POSTS_CATEGORY_ID"
private const val CONFIG_LAST_TIMESTAMP = "lastTimestamp"
private const val ACTION_GET_POSTS_WITH_DELAY = "pl.zhp.natropie.ui.action.GET_POSTS_WITH_DELAY"


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * helper methods.
 */

class ContentService : IntentService("ContentService") {

    private lateinit var db: NaTropieDB
    private lateinit var categoriesService: CategoriesService
    private lateinit var postsService: PostsService
    private lateinit var config: SharedPreferences

    override fun onCreate() {
        db = NaTropieDB.getInstance(this)!!
        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, DateSerializer()).create()
        val rf = Retrofit.Builder().baseUrl(resources.getString(R.string.NT_API_URL)).addConverterFactory(
            GsonConverterFactory.create(gson)
        ).build()
        categoriesService = rf.create(CategoriesService::class.java)
        postsService = rf.create(PostsService::class.java)
        config = getSharedPreferences("contentUpdatesSettings", MODE_PRIVATE)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_GET_MENU -> {
                handleGetMenu()
            }
            ACTION_GET_POSTS -> {
                val categoryId = intent.getIntExtra(ACTION_GET_POSTS_CATEGORY_ID, 0)
                handleGetPosts(categoryId)
            }
            ACTION_GET_POSTS_WITH_DELAY->{
                val categoryId = intent.getIntExtra(ACTION_GET_POSTS_CATEGORY_ID, 0)
                handleGetPostsWithDelay(categoryId)
            }
        }
    }

    private fun handleGetPostsWithDelay(categoryId: Int) {
        handleGetPosts(categoryId,DELAY)
    }

    private fun handleGetPosts(categoryId: Int, timeOffset:Long = 0) {
        if (!isNetworkAvailable()){
            sendResponse<NothingResponse>(RESPONSE_GET_POSTS)
            return
        }
        val postsList: List<Post>? = if (categoryId == 0) {
            val lastTimestamp = config.getLong(CONFIG_LAST_TIMESTAMP, 0)
            val currentTimestamp = System.currentTimeMillis() / 1000
            if (lastTimestamp < currentTimestamp - timeOffset){
                Track.DownloadPosts(lastTimestamp)
                config.edit().putLong(CONFIG_LAST_TIMESTAMP, currentTimestamp).apply()
                postsService.getFromMainPage(lastTimestamp).execute().body()
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }

        val table = db.postsRepository()
        for (post in postsList!!.iterator()) {
            table.insert(post)
        }

        sendResponse<NothingResponse>(RESPONSE_GET_POSTS)
    }

    /**
     * Get menu from db or internet
     */
    private fun handleGetMenu() {
        var table = db.categoriesRepository()
        if (isNetworkAvailable()){
            val categoriesList = categoriesService.listCategories().execute().body()
            for (cc in categoriesList!!.iterator()) {
                try {

                    table.insert(cc)

                } catch (e: Exception) {

                }
            }
        }
        val categories = table.getAllForMenu().toTypedArray()
        sendResponse(RESPONSE_GET_MENU, listOf(ContentParam<Category>().apply {
            Name = RESPONSE_VAR_MENU
            Data = categories
        }))
    }

    private fun <T : AEntity> sendResponse(broadcastName: String, list: List<ContentParam<T>>? = null) {
        val intent = Intent(broadcastName).apply {
            if (list != null) {
                for (param in list) {
                    putExtra(param.Name, param.GetParcerable())
                }
            }
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    companion object {
        const val RESPONSE_GET_MENU = "pl.zhp.natropie.services.ContentService.RESPONSE_GET_MENU"
        const val RESPONSE_VAR_MENU = "pl.zhp.natropie.services.ContentService.RESPONSE_GET_MENU.VAR_MENU"
        const val RESPONSE_GET_POSTS = "pl.zhp.natropie.services.ContentService.RESPONSE_GET_POSTS"
        const val RESPONSE_VAR_POSTS = "pl.zhp.natropie.services.ContentService.RESPONSE_GET_POSTS"
        const val DELAY:Long = 120*60

        fun listenGetMenu(context: Context, callback: (context: Context?, Intent?) -> Unit) {
            listen(
                RESPONSE_GET_MENU,
                callback,
                context
            )
        }

        fun listenGetPosts(context: Context, callback: (context: Context?, Intent?) -> Unit) {
            listen(
                RESPONSE_GET_POSTS,
                callback,
                context
            )
        }

        private fun listen(name: String, callback: (context: Context?, Intent?) -> Unit, context: Context) {
            val filter = IntentFilter().apply {
                addAction(name)
            }
            val receiver = ContentServiceListener(callback)
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)
        }

        @JvmStatic
        fun startGetMenu(context: Context) {
            val intent = Intent(context, ContentService::class.java).apply {
                action = ACTION_GET_MENU
            }
            context.startService(intent)
        }

        @JvmStatic
        fun getPosts(context: Context, categoryId: Int = 0) {
            val intent = Intent(context, ContentService::class.java).apply {
                action = ACTION_GET_POSTS
                putExtra(ACTION_GET_POSTS_CATEGORY_ID, categoryId)
            }
            context.startService(intent)
        }

        fun getPostsWithDelay(context: Context, categoryId: Int = 0) {
            val intent = Intent(context, ContentService::class.java).apply {
                action = ACTION_GET_POSTS_WITH_DELAY
                putExtra(ACTION_GET_POSTS_CATEGORY_ID, categoryId)
            }
            context.startService(intent)
        }

    }
}
