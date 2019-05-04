package pl.zhp.natropie.ui

import android.os.Parcelable
import org.parceler.Parcels

class ContentParam<T> {
    fun GetParcerable(): Array<Parcelable> {
        return Data!!.map {
            Parcels.wrap(it)
        }.toTypedArray()
    }

    var Name:String? = null
    var Data:Array<T>? =null
}