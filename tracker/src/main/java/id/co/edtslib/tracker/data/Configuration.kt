package id.co.edtslib.tracker.data

data class Configuration (
    var sessionId: String,
    var userId: Long,
    var installReferer: InstallReferer? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var eventId: Long = 1,
    var previousPageName: String? = null
)