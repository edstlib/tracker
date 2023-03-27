package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerFilterCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_category")
    val eventCategory: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("filter_list")
    val list: List<TrackerFilterDetail>
) {
    companion object {
        fun create(eventId: Long, list: List<TrackerFilterDetail>) =
            TrackerFilterCore(eventName = "user_filter",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                eventCategory = "",
                pageName = Tracker.currentPageName,
                list = list)


    }

}