package pl.zhp.natropie.db

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class WebsiteDBAdapter(val ctx: Context) {

    private lateinit var helper: DBHelper

    private lateinit var db: SQLiteDatabase

    fun open(): WebsiteDBAdapter {
        helper = DBHelper(this,ctx, DB_NAME, null, DB_VERSION)
        db = try{
            helper.writableDatabase
        } catch(e: SQLException){
            helper.readableDatabase
        }
        return this
    }

    fun close(){
        helper.close()
    }

    fun persist(tableName:String, values:ContentValues){
        db.insert(tableName, null, values)
    }

    fun exec(sql: String) {
        db.execSQL(sql)
    }

    companion object {
        fun Instance(applicationContext: Context): WebsiteDBAdapter {
            if (instance==null){
                instance = WebsiteDBAdapter(applicationContext)
                instance!!.open()
            }
            return instance as WebsiteDBAdapter
        }
        fun Close(){
            if (instance!=null){
                instance!!.close()
                instance = null
            }
        }
        private var instance:WebsiteDBAdapter? = null

        val DEBUG_TAG: String = "WebsiteDBAdapter"
        val DB_VERSION: Int = 1
        val DB_NAME: String = "storage.db"
    }

}