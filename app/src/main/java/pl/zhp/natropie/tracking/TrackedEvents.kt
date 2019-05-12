package pl.zhp.natropie.tracking

import com.google.firebase.analytics.FirebaseAnalytics

object TrackedEvents {
    const val APP_OPEN = FirebaseAnalytics.Event.APP_OPEN
    const val DOWNLOAD_POSTS: String = "E_DOWNLOAD_POSTS"
    const val DISPLAY_POSTS: String = "E_DISPLAY_POSTS"
    const val DISPLAY_POST: String = "E_DISPLAY_POST"
}