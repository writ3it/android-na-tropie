package pl.zhp.natropie.tracking

import com.google.firebase.analytics.FirebaseAnalytics

object TrackedParams {
    const val TIMESTAMP = "P_TIMESTAMP"
    const val CATEGORY: String = "P_CATEGORY"
    const val POST_ID: String = FirebaseAnalytics.Param.ITEM_ID
    const val POST_TITLE: String = FirebaseAnalytics.Param.ITEM_NAME
    const val POST_AUTHOR: String = FirebaseAnalytics.Param.ITEM_BRAND
    const val POST_CATEGORY: String = FirebaseAnalytics.Param.ITEM_CATEGORY
}
