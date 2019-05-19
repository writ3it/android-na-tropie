package pl.zhp.natropie.tracking

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import pl.zhp.natropie.services.ContentParam

object Track{


        private var FA: FirebaseAnalytics? = null

        fun initializeWithContext(applicationContext: Context){
            FA = FirebaseAnalytics.getInstance(applicationContext)
            appOpen()
        }
        private fun appOpen(){
            val bundle = Bundle()
            FA!!.logEvent(Event.APP_OPEN,bundle)
        }

        fun DownloadPosts(timestamp:Long){
            val bundle = Bundle().apply {
                putLong(Param.TIMESTAMP,timestamp)
            }
            FA!!.logEvent(Event.DOWNLOAD_POSTS,bundle)
        }

        fun DisplayList(categoryName:String){
            val bundle = Bundle().apply {
                putString(Param.CATEGORY,categoryName)
            }
            FA!!.logEvent(Event.DISPLAY_POSTS,bundle)
        }

        fun DisplayPost(postId:Long, postTitle:String, postAuthor:String, postCategory:String){
            val bundle=Bundle().apply{
                putLong(Param.POST_ID,postId)
                putString(Param.POST_TITLE, postTitle)
                putString(Param.POST_AUTHOR, postAuthor)
                putString(Param.POST_CATEGORY, postCategory)
            }
            FA!!.logEvent(Event.DISPLAY_POST,bundle)
        }


    val Event = TrackedEvents
    val Param= TrackedParams
    const val MAINPAGE: String = "Strona główna"

}

class TrackParam(val Name:String, val Value:String) {

}
