package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerPageDetailCore (
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
    @SerializedName("details")
    val details: Any?
) {
    companion object {
        fun create(eventId: Long, details: Any?) =
            TrackerPageDetailCore(eventName = "page_detail",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                pageName = Tracker.currentPageName,
                eventId = eventId,
                details = details)
    }
}