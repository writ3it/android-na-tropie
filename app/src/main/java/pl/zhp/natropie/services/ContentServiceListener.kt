package pl.zhp.natropie.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ContentServiceListener(val callback:(context: Context?,Intent?)->Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        callback(context, intent)
    }

}