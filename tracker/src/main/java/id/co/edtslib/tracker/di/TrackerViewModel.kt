package id.co.edtslib.tracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

open class TrackerViewModel(
    private val trackerUseCase: TrackerUseCase
): ViewModel() {
    fun createSession() = trackerUseCase.createSession().asLiveData()
    fun setUserId(userId: Long) = trackerUseCase.setUserId(userId).asLiveData()

    fun trackStartApplication() = trackerUseCase.trackStartApplication().asLiveData()
    fun trackExitApplication() = trackerUseCase.trackExitApplication().asLiveData()

    fun trackPage(screenName: String) = trackerUseCase.trackPage(screenName).asLiveData()
    fun trackPageDetail(name: String, detail: Any?)=
        trackerUseCase.trackPageDetail(name, detail).asLiveData()

    fun trackClick(name: String) = trackerUseCase.trackClick(name).asLiveData()
    fun trackFilters(name: String, filters: List<String>) =
        trackerUseCase.trackFilters(name, filters).asLiveData()
    fun trackSort(name: String, sortType: String) =
        trackerUseCase.trackSort(name, sortType).asLiveData()

    fun trackSubmission(name: String, status: Boolean, reason: String?) =
        trackerUseCase.trackSubmission(name, status, reason).asLiveData()
}