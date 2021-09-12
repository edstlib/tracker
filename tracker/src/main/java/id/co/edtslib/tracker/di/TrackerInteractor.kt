package id.co.edtslib.tracker.di

import kotlinx.coroutines.flow.Flow

class TrackerInteractor(private val repository: ITrackerRepository): TrackerUseCase {
    override fun createSession() = repository.createSession()
    override fun setUserId(userId: Long) = repository.setUserId(userId)

    override fun trackStartApplication() = repository.trackStartApplication()
    override fun trackExitApplication() = repository.trackExitApplication()

    override fun trackPage(name: String) = repository.trackPage(name)
    override fun trackPageDetail(name: String, detail: Any?) = repository.trackPageDetail(name, detail)

    override fun trackClick(name: String) = repository.trackClick(name)
    override fun trackFilters(name: String, filters: List<String>) = repository.trackFilters(name, filters)
    override fun trackSort(name: String, sortType: String) = repository.trackSort(name, sortType)

    override fun trackSubmission(name: String, status: Boolean, reason: String?) = repository.trackSubmission(name, status, reason)

}