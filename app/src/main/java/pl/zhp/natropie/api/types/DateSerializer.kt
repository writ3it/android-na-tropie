package pl.zhp.natropie.api.types

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import pl.zhp.natropie.db.types.DateConverter
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateSerializer : JsonDeserializer<Date>{

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        val format = SimpleDateFormat(formatString)
        try{
            return format.parse(json?.asString)
        } catch(e:ParseException){
            Log.e("DateSerializer","Cannot parse date")
            return null
        }
    }

    companion object {
        const val formatString = "yyyy-MM-dd'T'HH:mm:ss"
    }

}