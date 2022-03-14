package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerResponse
import kotlinx.coroutines.flow.Flow

interface TrackerUseCase {
    fun createSession(): Flow<String?>
    fun setUserId(userId: Long): Flow<Long>
    fun setInstallReferer(installReferer: InstallReferer): Flow<Boolean>

    fun trackStartApplication(): Flow<TrackerResponse>
    fun trackExitApplication(): Flow<Boolean>

    fun trackPage(name: String): Flow<TrackerResponse>
    fun trackPageDetail(name: String, detail: Any?): Flow<TrackerResponse>

    fun trackClick(name: String): Flow<TrackerResponse>
    fun trackFilters(name: String, filters: List<String>): Flow<TrackerResponse>
    fun trackSort(name: String, sortType: String): Flow<TrackerResponse>

    fun trackImpression(name: String, data: Any): Flow<TrackerResponse>
    fun trackSubmission(name: String, status: Boolean, reason: String?): Flow<TrackerResponse>

}