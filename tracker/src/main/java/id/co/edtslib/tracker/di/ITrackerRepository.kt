package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerResponse
import kotlinx.coroutines.flow.Flow

interface ITrackerRepository {
    fun createSession(): Flow<String?>
    fun setUserId(userId: Long): Flow<Long>
    fun setLatLng(lat: Double?, lng: Double?): Flow<Boolean>

    fun setInstallReferer(installReferer: InstallReferer): Flow<Boolean>

    fun trackStartApplication(): Flow<TrackerResponse>
    fun trackExitApplication(): Flow<Boolean>

    fun trackPage(pageName: String, pageId: String): Flow<TrackerResponse>
    fun trackPageDetail(detail: Any?): Flow<TrackerResponse>

    fun trackClick(name: String, url: String? = null, details: Any? = null): Flow<TrackerResponse>
    fun trackFilters(filters: List<String>): Flow<TrackerResponse>
    fun trackSort(sortType: String): Flow<TrackerResponse>

    fun trackImpression(data: Any): Flow<TrackerResponse>
    fun trackSubmission(name: String, category: String, status: Boolean, reason: String?, details: Any? = null): Flow<TrackerResponse>

    fun trackDisplayedItems(data: Any): Flow<TrackerResponse>
    fun trackSearch(keyword: String, details: Any? = null): Flow<TrackerResponse>

}