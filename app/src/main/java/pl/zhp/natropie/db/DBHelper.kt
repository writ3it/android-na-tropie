package pl.zhp.natropie.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import pl.zhp.natropie.db.tables.Categories

class DBHelper(val adapter:WebsiteDBAdapter, context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(WebsiteDBAdapter.DEBUG_TAG, "Database creating...");
        val categories = Categories(adapter)
        categories.OnCreate()
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(WebsiteDBAdapter.DEBUG_TAG, "Database upgradeing...");
        val categories = Categories(adapter)
        categories.OnUpgrade()
    }
}