package id.co.edtslib.tracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.co.edtslib.tracker.data.InstallReferer

open class TrackerViewModel(
    private val trackerUseCase: TrackerUseCase
): ViewModel() {
    fun createSession() = trackerUseCase.createSession().asLiveData()
    fun setUserId(userId: Long) = trackerUseCase.setUserId(userId).asLiveData()
    fun setLatLng(lat: Double?, lng: Double?) = trackerUseCase.setLatLng(lat, lng).asLiveData()
    fun setInstallReferer(installReferer: InstallReferer) =
        trackerUseCase.setInstallReferer(installReferer).asLiveData()

    fun trackOpenApplication() = trackerUseCase.trackApplication("open_app").asLiveData()
    fun trackResumeApplication() = trackerUseCase.trackApplication("resume_app").asLiveData()
    fun trackMinimizeApplication() = trackerUseCase.trackApplication("minimize_app").asLiveData()
    fun trackCloseApplication() = trackerUseCase.trackApplication("close_app").asLiveData()

    fun trackPage(pageName: String, pageId: String) =
        trackerUseCase.trackPage(pageName, pageId).asLiveData()
    fun trackPageDetail(detail: Any?)=
        trackerUseCase.trackPageDetail(detail).asLiveData()

    fun trackClick(name: String, category: String? = null, url: String? = null, details: Any? = null) =
        trackerUseCase.trackClick(name, category, url, details).asLiveData()
    fun trackFilters(filters: List<String>) =
        trackerUseCase.trackFilters(filters).asLiveData()
    fun trackSort(sortType: String) =
        trackerUseCase.trackSort(sortType).asLiveData()

    fun trackSubmission(name: String, category: String, status: Boolean, reason: String?, details: Any? = null) =
        trackerUseCase.trackSubmission(name, category, status, reason, details).asLiveData()

    fun trackImpression(data: Any) =
        trackerUseCase.trackImpression(data).asLiveData()

    fun trackDisplayedItems(data: Any) =
        trackerUseCase.trackDisplayedItems(data).asLiveData()

    fun trackSearch(keyword: String, details: Any? = null) =
        trackerUseCase.trackSearch(keyword,details).asLiveData()

}