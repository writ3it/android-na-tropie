package pl.zhp.natropie.db.types

import android.icu.text.SimpleDateFormat
// @TODO: skończyć
class Converters {
    companion object {
        fun fromTimestamp(value:Long)->String{
            //val date =
            return ""
        }
        fun dateToTimestamp(date:String)->Long{
            val dateObj = SimpleDateFormat("yyyy-MM-ddThh:ii:ss").parse(date)
            return dateObj.time
        }
    }
}