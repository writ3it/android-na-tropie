package pl.zhp.natropie.ui

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.AEntity
import pl.zhp.natropie.db.entities.Category

private const val ACTION_GET_MENU = "pl.zhp.natropie.ui.action.GET_MENU"



/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * helper methods.
 */
class ContentService : IntentService("ContentService") {

    private var db: NaTropieDB? = null

    override fun onCreate(){
        db = NaTropieDB.getInstance(this)
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
        val categories = db?.categoriesRepository()?.getAll()?.toTypedArray()
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
