package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName="categories")
data class Category(
    @PrimaryKey(autoGenerate=false)
    var id:Int,
    @ColumnInfo(name="name")
    var name:String) {


}