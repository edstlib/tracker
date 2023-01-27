package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerActivityCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: Long,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("activity_details")
    val activityDetails: String
) {
    companion object {
        fun createPageResume() =
            TrackerActivityCore(eventName = "app_activity",
                eventTimeStamp = Date().time, pageViewId = "resume_${Date().time}",
                eventId = Tracker.eventId++, activityDetails = "resume_app")

        fun createPageExit() =
            TrackerActivityCore(eventName = "app_activity",
                eventTimeStamp = Date().time, pageViewId = "resume_${Date().time}",
                eventId = Tracker.eventId++, activityDetails = "exit_app")

    }

}