package pl.zhp.natropie.api.responses

import org.parceler.Parcel
import org.parceler.ParcelConstructor
import pl.zhp.natropie.db.entities.Category

@Parcel(Parcel.Serialization.BEAN)
class CategoryResponse @ParcelConstructor constructor(id: Long, name: String, menu: Boolean, box_color: String, order:Int) :
    Category(id, name, menu, box_color,order) {

}
