package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerSortCore (
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
    @SerializedName("sort_type")
    val sortType: String
) {
    companion object {
        fun create(sortType: String) =
            TrackerSortCore(eventName = "user_sort",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = Tracker.eventId++,
                pageName = Tracker.currentPageName,
                sortType = sortType)


    }

}