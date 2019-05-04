package pl.zhp.natropie.db.tables

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import pl.zhp.natropie.db.WebsiteDBAdapter
import pl.zhp.natropie.db.entities.AEntity

class Categories(db: WebsiteDBAdapter) : ATable(db) {

    override fun GetName(): String {
        return NAME
    }

    override fun OnCreate(){
        db.exec(CREATION_SQL)
    }

    override fun OnUpgrade(){
        db.exec(DROP_SQL)
        OnCreate()
    }

    companion object {
        val NAME:String = "categories"
        val COLUMN_ID:String ="id"
        val COLUMN_ID_OPTIONS:String = "INTEGER PRIMARY KEY"
        val COLUMN_NAME:String="name"
        val COLUMN_NAME_OPTIONS:String = "VARCHAR(255)"
        val CREATION_SQL:String = "CREATE TABLE $NAME( $COLUMN_ID $COLUMN_ID_OPTIONS, $COLUMN_NAME $COLUMN_NAME_OPTIONS);"
        val DROP_SQL:String = "DROP TABLE IF EXISTS $NAME;"
    }
}