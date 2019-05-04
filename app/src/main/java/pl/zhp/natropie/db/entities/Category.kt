package pl.zhp.natropie.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.parceler.Parcel
import org.parceler.ParcelConstructor


@Parcel(Parcel.Serialization.BEAN)
@Entity(tableName="categories")
data class Category @ParcelConstructor constructor(
    @PrimaryKey(autoGenerate=false)
    var id:Int,
    @ColumnInfo(name="name")
    var name:String,
    @ColumnInfo(name="parent")
    var parent:Int,
    @ColumnInfo(name="menu")
    var mainMenu:Boolean = false
) : AEntity {

}