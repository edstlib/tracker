package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerClickLinkCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: Long,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_category")
    val eventCategory: String,
    @SerializedName("link_label")
    val linkName: String,
    @SerializedName("link_url")
    val linkUrl: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("details")
    val details: Any?,
) {
    companion object {
        fun create(name: String, details: Any? = null) =
            TrackerClickLinkCore(eventName = "click_link",
                eventTimeStamp = Date().time, pageViewId = Tracker.currentPageId,
                eventId = Tracker.eventId++, eventCategory = "", linkName = name,
                linkUrl = "", pageName = Tracker.currentPageName, details = details)
    }
}