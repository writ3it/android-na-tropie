package pl.zhp.natropie.api.responses

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
class CategoryResponse @ParcelConstructor constructor(
    var id:Int,
    var acf:HashMap<String, String>
) {

}
