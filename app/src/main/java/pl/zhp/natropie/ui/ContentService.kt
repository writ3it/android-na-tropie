package pl.zhp.natropie.ui

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.gson.GsonBuilder
import pl.zhp.natropie.R
import pl.zhp.natropie.api.CategoriesMetaService
import pl.zhp.natropie.api.CategoriesService
import pl.zhp.natropie.api.PostsService
import pl.zhp.natropie.api.types.DateSerializer
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.AEntity
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val ACTION_GET_MENU = "pl.zhp.natropie.ui.action.GET_MENU"
private const val ACTION_GET_POSTS = "pl.zhp.natropie.ui.action.GET_POSTS"
private const val ACTION_GET_POSTS_CATEGORY_ID = "pl.zhp.natropie.ui.action.ACTION_GET_POSTS_CATEGORY_ID"


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * helper methods.
 */

class ContentService : IntentService("ContentService") {

    private var db: NaTropieDB? = null

    private var categoriesService: CategoriesService? = null

    private var categoriesMetaService: CategoriesMetaService? = null

    private var postsService: PostsService? = null

    override fun onCreate(){
        db = NaTropieDB.getInstance(this)
        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, DateSerializer()).create()
        val rf = Retrofit.Builder().baseUrl(resources.getString(R.string.API_URL)).addConverterFactory(
            GsonConverterFactory.create(gson)
        ).build()
        val rfMeta = Retrofit.Builder().baseUrl(resources.getString(R.string.META_API_URL)).addConverterFactory(
            GsonConverterFactory.create(gson)
        ).build()
        categoriesService = rf.create(CategoriesService::class.java)
        categoriesMetaService = rfMeta.create(CategoriesMetaService::class.java)
        postsService = rf.create(PostsService::class.java)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_GET_MENU -> {
                handleGetMenu()
            }
            ACTION_GET_POSTS->{
                val categoryId = intent.getIntExtra(ACTION_GET_POSTS_CATEGORY_ID , 0)
                handleGetPosts(categoryId)
            }
        }
    }

    private fun handleGetPosts(categoryId: Int) {
        val postsList:List<Post>? = if (categoryId == 0){
            postsService!!.getFromMainPage().execute().body()?.map { it.toPost() }
        } else {
            emptyList()
        }

        val table = db?.postsRepository()
        for (post in postsList!!.iterator()){
            table!!.insert(post)
        }
        sendResponse(RESPONSE_GET_POSTS, listOf(ContentParam<Post>().apply{
            Name= RESPONSE_VAR_POSTS
            Data = postsList.toTypedArray()
        }))
    }

    /**
     * Get menu from db or internet
     */
    private fun handleGetMenu() {
        // @TODO: przerobić na 1 request
        val categoriesList  = categoriesService!!.listCategories().execute().body()
        val categoriesMetaService = categoriesMetaService!!.listOfAllMetas().execute().body()
        var table = db?.categoriesRepository()
        for( cc in categoriesList!!.iterator()){
            for( meta in categoriesMetaService!!.iterator()){
                if (cc.id == meta.id){
                    if (meta.acf.containsKey("main_menu")){
                        if (meta.acf["main_menu"]=="true"){
                            cc.mainMenu = true
                        }
                    }
                }
            }
            table!!.insert(cc)
        }
        val categories = table!!.getAllForMenu()?.toTypedArray()
        sendResponse(RESPONSE_GET_MENU, listOf(ContentParam<Category>().apply{
            Name = RESPONSE_VAR_MENU
            Data = categories
        }))
    }

    private fun <T: AEntity>sendResponse(broadcastName: String, list: List<ContentParam<T>>) {
        val intent = Intent(broadcastName).apply {
            for(param in list){
                putExtra(param.Name,param.GetParcerable())
            }
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }


    companion object {
        const val RESPONSE_GET_MENU = "pl.zhp.natropie.ui.ContentService.RESPONSE_GET_MENU"
        const val RESPONSE_VAR_MENU = "pl.zhp.natropie.ui.ContentService.RESPONSE_GET_MENU.VAR_MENU"
        const val RESPONSE_GET_POSTS = "pl.zhp.natropie.ui.ContentService.RESPONSE_GET_POSTS"
        const val RESPONSE_VAR_POSTS = "pl.zhp.natropie.ui.ContentService.RESPONSE_GET_POSTS"

        fun listenGetMenu(context:Context,callback:(context: Context?,Intent?)->Unit){
            listen(RESPONSE_GET_MENU, callback,context)
        }

        fun listenGetPosts(context:Context,callback:(context: Context?,Intent?)->Unit){
            listen(RESPONSE_GET_POSTS, callback,context)
        }
        private fun listen(name:String, callback:(context: Context?,Intent?)->Unit,context:Context){
            val filter = IntentFilter().apply{
                addAction(name)
            }
            val receiver = ContentServiceListener(callback)
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver,filter)
        }
        @JvmStatic
        fun startGetMenu(context: Context) {
            val intent = Intent(context, ContentService::class.java).apply {
                action = ACTION_GET_MENU
            }
            context.startService(intent)
        }
        @JvmStatic
        fun getPosts(context:Context, categoryId: Int= 0 ){
            val intent = Intent(context,ContentService::class.java).apply {
                action = ACTION_GET_POSTS
                putExtra(ACTION_GET_POSTS_CATEGORY_ID, categoryId)
            }
            context.startService(intent)
        }

    }
}
