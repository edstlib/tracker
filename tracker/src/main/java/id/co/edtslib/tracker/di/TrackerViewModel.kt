package id.co.edtslib.tracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.co.edtslib.tracker.data.InstallReferer

open class TrackerViewModel(
    private val trackerUseCase: TrackerUseCase
): ViewModel() {
    fun createSession() = trackerUseCase.createSession().asLiveData()
    fun setUserId(userId: Long) = trackerUseCase.setUserId(userId).asLiveData()
    fun setLatLng(lat: Double, lng: Double) = trackerUseCase.setLatLng(lat, lng).asLiveData()
    fun setInstallReferer(installReferer: InstallReferer) =
        trackerUseCase.setInstallReferer(installReferer).asLiveData()

    fun trackStartApplication() = trackerUseCase.trackStartApplication().asLiveData()
    fun trackExitApplication() = trackerUseCase.trackExitApplication().asLiveData()

    fun trackPage(pageName: String, pageId: String) =
        trackerUseCase.trackPage(pageName, pageId).asLiveData()
    fun trackPageDetail(detail: Any?)=
        trackerUseCase.trackPageDetail(detail).asLiveData()

    fun trackClick(name: String) = trackerUseCase.trackClick(name).asLiveData()
    fun trackFilters(filters: List<String>) =
        trackerUseCase.trackFilters(filters).asLiveData()
    fun trackSort(sortType: String) =
        trackerUseCase.trackSort(sortType).asLiveData()

    fun trackSubmission(name: String, status: Boolean, reason: String?) =
        trackerUseCase.trackSubmission(name, status, reason).asLiveData()

    fun trackImpression(data: Any) =
        trackerUseCase.trackImpression(data).asLiveData()

    fun trackDisplayedItems(data: Any) =
        trackerUseCase.trackDisplayedItems(data).asLiveData()

    fun trackSearch(keyword: String) =
        trackerUseCase.trackSearch(keyword).asLiveData()

}