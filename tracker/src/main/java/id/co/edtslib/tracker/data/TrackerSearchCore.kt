package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerSearchCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("search_input")
    val keyword: String,
    @SerializedName("details")
    val details: Any?
) {
    companion object {
        fun create(keyword: String, details: Any? = null) =
            TrackerSearchCore(eventName = "user_search",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = Tracker.eventId++,
                pageName = Tracker.currentPageName,
                keyword = keyword,
                details = details)
    }
}