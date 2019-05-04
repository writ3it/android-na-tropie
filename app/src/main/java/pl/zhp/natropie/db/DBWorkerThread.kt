package pl.zhp.natropie.db

import android.os.Handler
import android.os.HandlerThread

class DBWorkerThread(name:String):HandlerThread(name) {
    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        mWorkerHandler.post(task)
    }

    companion object {
        fun InsertTask(thread:DBWorkerThread, task:Runnable){
            thread.postTask(task)
        }
    }
}