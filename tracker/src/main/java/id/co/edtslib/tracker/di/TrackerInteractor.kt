package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.InstallReferer

class TrackerInteractor(private val repository: ITrackerRepository): TrackerUseCase {
    override fun createSession() = repository.createSession()
    override fun setUserId(userId: Long) = repository.setUserId(userId)
    override fun setLatLng(lat: Double?, lng: Double?) = repository.setLatLng(lat, lng)
    override fun setInstallReferer(installReferer: InstallReferer) =
        repository.setInstallReferer(installReferer)

    override fun trackStartApplication() = repository.trackStartApplication()
    override fun trackExitApplication() = repository.trackExitApplication()

    override fun trackPage(pageName: String, pageId: String) =
        repository.trackPage(pageName, pageId)
    override fun trackPageDetail(detail: Any?) = repository.trackPageDetail(detail)

    override fun trackClick(name: String, category: String?, url: String?, details: Any?) =
        repository.trackClick(name, category, url, details)
    override fun trackFilters(filters: List<String>) = repository.trackFilters(filters)
    override fun trackSort(sortType: String) = repository.trackSort(sortType)

    override fun trackImpression(data: Any) = repository.trackImpression(data)
    override fun trackSubmission(name: String, category: String, status: Boolean, reason: String?, details: Any?) =
        repository.trackSubmission(name, category, status, reason, details)

    override fun trackDisplayedItems(data: Any) = repository.trackDisplayedItems(data)
    override fun trackSearch(keyword: String, details: Any?) = repository.trackSearch(keyword, details)

}