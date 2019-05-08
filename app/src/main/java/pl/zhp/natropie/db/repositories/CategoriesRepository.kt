package pl.zhp.natropie.db.repositories

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import pl.zhp.natropie.db.entities.Category

@Dao
interface CategoriesRepository{

    @Query("Select * from categories")
    fun getAll():List<Category>

    @Query("Select * from categories where menu=1")
    fun getAllForMenu():List<Category>


    @Insert(onConflict=REPLACE)
    fun insert(data:Category)

    @Query("DELETE FROM categories")
    fun deletAll()

    @Query("SELECT * FROM categories where id = :id")
    fun get(id: Int): Category
}