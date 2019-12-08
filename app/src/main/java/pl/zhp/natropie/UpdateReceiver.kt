package pl.zhp.natropie

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Environment
import android.widget.Toast
import java.nio.file.Files.delete
import java.nio.file.Files.isDirectory
import android.os.Environment.getExternalStorageDirectory
import java.io.File


class UpdateReceiver : BroadcastReceiver() {
    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * [android.content.Context.registerReceiver]. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     *
     *
     * **If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.** This means you should not perform any operations that
     * return a result to you asynchronously. If you need to perform any follow up
     * background work, schedule a [android.app.job.JobService] with
     * [android.app.job.JobScheduler].
     *
     * If you wish to interact with a service that is already running and previously
     * bound using [bindService()][android.content.Context.bindService],
     * you can use [.peekService].
     *
     *
     * The Intent filters used in [android.content.Context.registerReceiver]
     * and in application manifests are *not* guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, [onReceive()][.onReceive]
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */

    private lateinit var config: SharedPreferences


    override fun onReceive(context: Context, intent: Intent?) {
        if (NEED_TO_CLEAR){
            clearApplicationData(context)
            deleteTempFolder()
        }
        if (NEED_TO_CLEAR_PERFS) {
            config = context.getSharedPreferences("contentUpdatesSettings", IntentService.MODE_PRIVATE)
            config.edit().clear().apply()
        }
    }

    fun clearApplicationData(context: Context) {
        val packageName = context.packageName
        val runtime = Runtime.getRuntime()
        runtime.exec("pm clear $packageName")
    }

    private fun deleteTempFolder() {
        val myDir = getExternalStorageDirectory()
        if (myDir.isDirectory) {
            val children = myDir.list()
            for (i in children.indices) {
                File(myDir, children[i]).delete()
            }
        }
    }

    companion object{
        val NEED_TO_CLEAR = true
        val NEED_TO_CLEAR_PERFS = true
    }
}