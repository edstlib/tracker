package id.co.edtslib.tracker.data

data class Configuration (
    var sessionId: String,
    var userId: Long,
    var installReferer: InstallReferer?,
    var latitude: Double? = null,
    var longitude: Double? = null
)