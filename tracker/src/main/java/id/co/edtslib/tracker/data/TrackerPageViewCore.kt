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
    val eventTimeStamp: String,
    @SerializedName("page_urlpath")
    val pageUrlPath: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("previous_page")
    val previousPage: String,
    @SerializedName("previous_page_urlpath")
    val previousPageUrlPath: String,

) {
    companion object {
        fun create(pageName: String, pageId: String): TrackerPageViewCore {
            val time = Date().time
            return TrackerPageViewCore(
                eventName = "page_view", eventTimeStamp = time.toString(),
                pageUrlPath = "", pageName = pageName, eventId = Tracker.eventId++,
                pageViewId = pageId, previousPage = "", previousPageUrlPath = ""
            )
        }
    }
}