package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerDisplayedItemCore (
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
    @SerializedName("item_list")
    val itemList: MutableList<Any>
) {
    companion object {
        fun create(eventId: Long, data: Any) =
            TrackerDisplayedItemCore(eventName = "displayed_item",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                pageName = Tracker.currentPageName,
                itemList = mutableListOf(data))
    }
}