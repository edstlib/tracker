package id.co.edtslib.tracker.data

class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService
) : BaseDataSource() {

    suspend fun send(trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(trackers) }

}