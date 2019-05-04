package pl.zhp.natropie.db.entities

import android.content.ContentValues

abstract class AEntity {
    abstract fun toValues():ContentValues
}