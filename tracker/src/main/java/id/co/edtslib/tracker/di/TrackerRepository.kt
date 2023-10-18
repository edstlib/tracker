package id.co.edtslib.tracker.di

import com.google.gson.Gson
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.*
import kotlinx.coroutines.flow.flow
import java.util.*

class TrackerRepository(
    private val remoteSource: TrackerRemoteDataSource,
    private val localSource: TrackerLocalDataSource,
    private val configurationLocalSource: ConfigurationLocalSource
) : ITrackerRepository {
    override fun createSession() = flow {
        val sessionId = String.format("%s-%d", UUID.randomUUID().toString(),
            Date().time)
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId, 0L, null)
        }
        else {
            configuration.sessionId = sessionId
        }
        configuration.previousPageName = null

        configurationLocalSource.save(configuration)

        emit(sessionId)
    }

    override fun setUserId(userId: Long) = flow {
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = userId,
                installReferer = null)
        }

        configuration.userId = userId
        configurationLocalSource.save(configuration)

        emit(userId)
    }

    override fun setLatLng(lat: Double?, lng: Double?) = flow {
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = 0L,
                installReferer = null,
                latitude = lat,
                longitude = lng)
        }

        configuration.latitude = lat
        configuration.longitude = lng
        configurationLocalSource.save(configuration)

        emit(true)
    }

    override fun setInstallReferer(installReferer: InstallReferer) = flow {
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(
                sessionId = "",
                userId = 0L,
                installReferer = installReferer)
        }
        configuration.installReferer = installReferer
        configurationLocalSource.save(configuration)

        emit(true)
    }

    override fun getInstallReferer(): InstallReferer? {
        val configuration = configurationLocalSource.getCached()
        return configuration?.installReferer
    }

    override fun trackApplication(eventName: String) = flow {
        val trackerCore = TrackerActivityCore.createPageActivity(
            eventId = configurationLocalSource.getEventId(),
            eventName = eventName,
            pageViewId = Tracker.currentPageId)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> {}
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
            }
            else -> {}
        }

        emit(true)
    }

    override fun getData(): TrackerData {
        val user = TrackerUser.create(configurationLocalSource.getSessionId(),
            configurationLocalSource.getUserId())
        val application = localSource.apps
        val marketing = configurationLocalSource.getCached()?.installReferer
        val network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
            configurationLocalSource.getLongitude())

        return TrackerData(core = false,
            user = user,
            application = application,
            marketing = marketing,
            network = network)
    }

    override fun trackPage(pageName: String, pageId: String) = flow {
        val previousPageName = configurationLocalSource.getPreviousPageName()

        val trackerCore = TrackerPageViewCore.create(
            eventId = configurationLocalSource.getEventId(),
            pageName = pageName,
            pageId = pageId,
            previousPage = previousPageName ?: ""
        )
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer
        )

        val trackerDataList = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDataList)

        configurationLocalSource.setPreviousPageName(pageName)

        when (response.status) {
            Result.Status.SUCCESS -> emit(
                TrackerResponse(
                    Gson().toJson(trackerData),
                    response.data
                )
            )
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDataList)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {
            }
        }
    }

    override fun trackPageDetail(detail: Any?) = flow {
        val trackerCore = TrackerPageDetailCore.create(
            eventId = configurationLocalSource.getEventId(),
            details = detail)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDataList = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDataList)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDataList)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackClick(name: String, category: String?, url: String?, details: Any?) = flow {
        val trackerCore = TrackerClickLinkCore.create(
            eventId = configurationLocalSource.getEventId(),
            name = name,
            category = category,
            url = url,
            details = details)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackFilters(filters: List<TrackerFilterDetail>) = flow {
        val trackerCore = TrackerFilterCore.create(
            eventId = configurationLocalSource.getEventId(),
            list = filters)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            application = localSource.apps,
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackSort(sortType: String) = flow {
        val trackerCore = TrackerSortCore.create(
            eventId = configurationLocalSource.getEventId(),
            sortType = sortType)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackImpression(category: String, time: Long, data: List<*>) = flow {
        val trackerCore = TrackerImpressionCore.create(
            eventId = configurationLocalSource.getEventId(),
            time = time,
            category= category,
            data = data)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackSubmission(
        name: String,
        category: String,
        status: Boolean,
        reason: String?,
        details: Any?
    ) = flow {
        val trackerCore = TrackerSubmissionCore.create(
            eventId = configurationLocalSource.getEventId(),
            label = name,
            category = category,
            status = status,
            reason = reason,
            details = details)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackDisplayedItems(
        data: Any
    ) = flow {
        val trackerCore = TrackerDisplayedItemCore.create(
            eventId = configurationLocalSource.getEventId(),
            data = data)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

    override fun trackSearch(
        keyword: String,
        details: Any?
    ) = flow {
        val trackerCore = TrackerSearchCore.create(
            eventId = configurationLocalSource.getEventId(),
            keyword = keyword,
            details = details)
        val trackerData = TrackerData(
            core = trackerCore,
            user = TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            application = localSource.apps,
            network = TrackerNetwork.create(configurationLocalSource.getLatitude(),
                configurationLocalSource.getLongitude()),
            marketing = configurationLocalSource.getCached()?.installReferer)

        val trackerDatas = TrackerDataList(mutableListOf(trackerData))

        val response = remoteSource.send(trackerDatas)
        when(response.status) {
            Result.Status.SUCCESS -> emit(TrackerResponse(Gson().toJson(trackerData), response.data))
            Result.Status.ERROR, Result.Status.UNAUTHORIZED -> {
                localSource.add(trackerDatas)
                emit(
                    TrackerResponse(Gson().toJson(trackerData), null)
                )
            }
            else -> {}
        }
    }

}