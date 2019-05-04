package pl.zhp.natropie.ui

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import pl.zhp.natropie.R
import pl.zhp.natropie.api.CategoriesMetaService
import pl.zhp.natropie.api.CategoriesService
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.AEntity
import pl.zhp.natropie.db.entities.Category
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ACTION_GET_MENU = "pl.zhp.natropie.ui.action.GET_MENU"



/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * helper methods.
 */

class ContentService : IntentService("ContentService") {

    private var db: NaTropieDB? = null

    private var categoriesService: CategoriesService? = null

    private var categoriesMetaService: CategoriesMetaService? = null

    override fun onCreate(){
        db = NaTropieDB.getInstance(this)
        val rf = Retrofit.Builder().baseUrl(resources.getString(R.string.API_URL)).addConverterFactory(
            GsonConverterFactory.create()).build()
        val rfMeta = Retrofit.Builder().baseUrl(resources.getString(R.string.META_API_URL)).addConverterFactory(
            GsonConverterFactory.create()).build()
        categoriesService = rf.create(CategoriesService::class.java)
        categoriesMetaService = rfMeta.create(CategoriesMetaService::class.java)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_GET_MENU -> {
                handleGetMenu()
            }

        }
    }

    /**
     * Get menu from db or internet
     */
    private fun handleGetMenu() {
        val categoriesList  = categoriesService!!.listCategories().execute().body()
        val categoriesMetaService = categoriesMetaService!!.listOfAllMetas().execute().body()
        var table = db?.categoriesRepository()
        for( cc in categoriesList!!.iterator()){
            for( meta in categoriesMetaService!!.iterator()){
                if (cc.id == meta.id){
                    if (meta.acf.containsKey("main_menu")){
                        if (meta.acf["main_menu"]=="true"){
                            table!!.insert(cc)
                        }
                    }
                }
            }

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
        fun listenGetMenu(context:Context,callback:(context: Context?,Intent?)->Unit){
            listen(RESPONSE_GET_MENU, callback,context)
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

    }
}
