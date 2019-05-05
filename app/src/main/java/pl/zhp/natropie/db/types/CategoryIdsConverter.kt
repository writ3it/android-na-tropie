package pl.zhp.natropie.db.types

import android.arch.persistence.room.TypeConverter


class CategoryIdsConverter {

    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.split(DELIMITER)?.map {
            it.toInt()
        }
    }

    @TypeConverter
    fun toString(value: List<Int>?): String? {
        return value?.joinToString(DELIMITER) { it.toString() }
    }
    companion object {
        const val DELIMITER = "/"
    }
}