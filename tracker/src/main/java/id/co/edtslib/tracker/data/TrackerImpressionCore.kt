package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackerImpressionCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: Long,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("impression_list")
    val impressionList: MutableList<Any>
) {
    companion object {
        fun create(screeName: String, data: Any) =
            TrackerImpressionCore("user_impression", Date().time, screeName,
                mutableListOf(data))
    }
}