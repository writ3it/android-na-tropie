package pl.zhp.natropie.db

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import pl.zhp.natropie.db.entities.Category
import pl.zhp.natropie.db.entities.Post
import pl.zhp.natropie.db.repositories.CategoriesRepository
import pl.zhp.natropie.db.repositories.PostsRepository
import pl.zhp.natropie.db.types.CategoryIdsConverter
import pl.zhp.natropie.db.types.DateConverter

@Database(entities= [Category::class, Post::class], version=14)
@TypeConverters(DateConverter::class,CategoryIdsConverter::class)
abstract class NaTropieDB : RoomDatabase(){

    abstract fun categoriesRepository(): CategoriesRepository
    abstract fun postsRepository(): PostsRepository

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