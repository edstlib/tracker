package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerPageViewCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_timestamp")
    val eventTimeStamp: Long,
    @SerializedName("page_urlpath")
    val pageUrlPath: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("pageview_id")
    val pageViewId: String,

) {
    companion object {
        fun create(pageName: String, pageId: String) =
            TrackerPageViewCore(eventName = "page_view", eventTimeStamp = Date().time,
                pageUrlPath = "", pageName = pageName, eventId = Tracker.eventId++,
                pageViewId = pageId)
    }
}