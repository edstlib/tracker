package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerImpressionCore (
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
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("impression_list")
    val impressionList: MutableList<Any>
) {
    companion object {
        fun create(data: Any) =
            TrackerImpressionCore(eventName = "user_impression",
                eventTimeStamp = Date().time,
                pageViewId = Tracker.currentPageId,
                eventId = Tracker.eventId++,
                eventCategory = "",
                pageName = Tracker.currentPageName,
                impressionList = mutableListOf(data))
    }
}