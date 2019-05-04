package pl.zhp.natropie.db.tables

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import pl.zhp.natropie.db.WebsiteDBAdapter
import pl.zhp.natropie.db.entities.AEntity

abstract class ATable(val db:WebsiteDBAdapter) {
    abstract fun GetName():String
    abstract fun OnCreate()
    abstract fun OnUpgrade()

    fun Persist(entity:AEntity){
        val values = entity.toValues()
        db.persist(GetName(), values)
    }
}