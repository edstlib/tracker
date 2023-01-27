package id.co.edtslib.tracker.data

data class Configuration (
    var sessionId: String,
    var userId: Long,
    var installReferer: InstallReferer?,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)