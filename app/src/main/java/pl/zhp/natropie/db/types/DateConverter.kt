package pl.zhp.natropie.db.types

import android.arch.persistence.room.TypeConverter
import android.util.Log
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
    companion object {
        fun fromTimestamp(value:Long):String{
            val v = value*1000
            return SimpleDateFormat("dd.MM.yyyy").format(Date(v))
        }
    }


}