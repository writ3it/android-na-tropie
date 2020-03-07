package pl.zhp.natropie.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.parceler.Parcels
import pl.zhp.natropie.MainActivity
import pl.zhp.natropie.R
import pl.zhp.natropie.ReaderActivity
import pl.zhp.natropie.db.NaTropieDB
import pl.zhp.natropie.db.entities.PostWithColor
import java.util.*
import kotlin.random.Random

class PushNotificationService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        ContentService.listenGetPosts(
            applicationContext,
            fun(context: Context?, intent: Intent?): Unit {
                GlobalScope.launch {
                    while (!stackToDisplay.empty()) {
                        val message = stackToDisplay.pop()
                        val postId = message.data[VAR_ITEM_ID]!!.toLong()
                        Log.i(TAG, "Fetched #$postId")
                        val post =
                            NaTropieDB.getInstance(applicationContext)?.postsRepository()?.get(
                                postId
                            )
                                ?: continue
                        notify(message, post)
                    }
                }
            })
    }

    private val stackToDisplay = Stack<RemoteMessage>()

    override fun onMessageReceived(message: RemoteMessage?) {
        Log.i(TAG, "Message received")
        displayNotification(message!!)
        super.onMessageReceived(message)
    }

    private fun displayNotification(message: RemoteMessage) {
        val data = message.data
        if (!data.containsKey(VAR_ITEM_ID)) {
            return
        }
        stackToDisplay.add(message)
        ContentService.getPosts(applicationContext)
    }

    private fun notify(message: RemoteMessage, post: PostWithColor?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(ReaderActivity.VAR_POST, Parcels.wrap(post))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val icon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.nt_small_logo)
        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.nt_push_icon)
            .setLargeIcon(icon)
            .setColor(Color.rgb(0, 0, 0))
            .setContentTitle("Na Tropie pisze!")
            .setContentText(message.notification!!.body)
            .setSound(sound)
            .setContentIntent(pIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(), builder.build())
        }
    }

    companion object {
        const val TAG = ">>> PushNotificationService"
        const val VAR_ITEM_ID = "item_id"
    }
}