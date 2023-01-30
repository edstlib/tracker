package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackerActivityCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("activity_details")
    val activityDetails: String
) {
    companion object {
        fun createPageActivity(eventId: Long, eventName: String) =
            TrackerActivityCore(eventName = "app_activity",
                eventTimeStamp = Date().time.toString(), pageViewId = "${eventName}_${Date().time}",
                eventId = eventId, activityDetails = eventName)


    }

}