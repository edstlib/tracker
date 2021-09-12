package id.co.edtslib.tracker.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.util.ConnectivityUtil

data class TrackerUser (
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("user_id")
    val userId: Long?,
    @SerializedName("user_ip_address")
    val userIpAddress: String?
){
    companion object {
        @SuppressLint("HardwareIds")
        fun create(sessionId: String?, userId: Long) : TrackerUser {
            return TrackerUser(
                sessionId, userId,
                ConnectivityUtil.getIPAddress(true)
            )
        }
    }
}