package pl.zhp.natropie.db.types

import android.arch.persistence.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    companion object {
        private const val format = "yyyy-MM-ddThh:mm:ss"

        @TypeConverter
        fun fromTimestamp(value:Long):String{
            val date = Date(value)
            return SimpleDateFormat(format).format(date)
        }
        @TypeConverter
        fun dateToTimestamp(date:String):Long{
            val dateObj = SimpleDateFormat(format).parse(date)
            return dateObj.time
        }
    }
}