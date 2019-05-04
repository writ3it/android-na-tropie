package pl.zhp.natropie.db.entities

import android.content.ContentValues
import pl.zhp.natropie.db.tables.Categories

class Category: AEntity() {
    override fun toValues():ContentValues {
        val values = ContentValues()
        values.put(Categories.COLUMN_ID, COLUMN_ID)
        values.put(Categories.COLUMN_NAME, COLUMN_NAME)
        return values
    }

    val COLUMN_ID:String="1"
    val COLUMN_NAME:String="Test"
}