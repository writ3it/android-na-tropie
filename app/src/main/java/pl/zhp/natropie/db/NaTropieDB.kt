package pl.zhp.natropie.db

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.repositories.CategoriesRepository

@Database(entities= [Category::class], version=1)
abstract class NaTropieDB : RoomDatabase(){

    abstract fun categoriesRepository(): CategoriesRepository

    companion object {
        private var INSTANCE: NaTropieDB? = null

        fun getInstance(context: Context): NaTropieDB? {
            if (INSTANCE == null) {
                synchronized(NaTropieDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NaTropieDB::class.java, "storage.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}