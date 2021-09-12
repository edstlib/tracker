package id.co.edtslib.tracker.data

data class TrackerData(
    val core: Any,
    val user: TrackerUser?,
    val application: TrackerApps?,
    val marketing: InstallReferer?
)