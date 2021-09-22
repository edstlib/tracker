package id.co.edtslib.tracker.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackerApiService {

    @POST("apps-tracker-gateway")
    suspend fun sendTracks(@Body track: TrackerDataList): Response<String>

}