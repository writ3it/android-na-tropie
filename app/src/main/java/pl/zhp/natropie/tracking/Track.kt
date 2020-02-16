package pl.zhp.natropie.tracking

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import pl.zhp.natropie.BuildConfig
import pl.zhp.natropie.services.ContentParam

object Track {


    private const val DEBUG_APP_ID: String = "pl.zhp.natropie.debug"
    private const val TAG = "***TRACKER***"
    private lateinit var FA: FirebaseAnalytics

    fun initializeWithContext(applicationContext: Context) {
        FA = FirebaseAnalytics.getInstance(applicationContext)
        displayInstanceId(applicationContext)
        appOpen()
    }

    private fun displayInstanceId(applicationContext: Context) {
        if (BuildConfig.APPLICATION_ID != DEBUG_APP_ID) {
            return
        }
        FA.appInstanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }

            // Get new Instance ID token
            var token = task.result
            if (token == null) {
                token = "undefined"
            }

            // Log and toast
            val msg = "Token: $token"
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

        })
    }

    private fun appOpen() {
        val bundle = Bundle()
        FA.logEvent(Event.APP_OPEN, bundle)
    }

    fun DownloadPosts(timestamp: Long) {
        val bundle = Bundle().apply {
            putLong(Param.TIMESTAMP, timestamp)
        }
        FA.logEvent(Event.DOWNLOAD_POSTS, bundle)
    }

    fun DisplayList(categoryName: String) {
        val bundle = Bundle().apply {
            putString(Param.CATEGORY, categoryName)
        }
        FA.logEvent(Event.DISPLAY_POSTS, bundle)
    }

    fun DisplayPost(postId: Long, postTitle: String, postAuthor: String, postCategory: String) {
        val bundle = Bundle().apply {
            putLong(Param.POST_ID, postId)
            putString(Param.POST_TITLE, postTitle)
            putString(Param.POST_AUTHOR, postAuthor)
            putString(Param.POST_CATEGORY, postCategory)
        }
        FA.logEvent(Event.DISPLAY_POST, bundle)
    }


    val Event = TrackedEvents
    val Param = TrackedParams
    const val MAINPAGE: String = "Strona główna"
    const val CLIPBOARD: String = "Schowek"

}

class TrackParam(val Name: String, val Value: String) {

}
