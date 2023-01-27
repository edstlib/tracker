package id.co.edtslib.tracker.di

import com.google.gson.Gson
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

    override fun trackStartApplication() = flow {
        val trackerCore = TrackerActivityCore.createPageResume()
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

    override fun trackExitApplication() = flow {
        val trackerCore = TrackerActivityCore.createPageExit()
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

    override fun trackPage(pageName: String, pageId: String) = flow {
        val trackerCore = TrackerPageViewCore.create(pageName, pageId)
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
        val trackerCore = TrackerPageDetailCore.create(detail)
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

    override fun trackClick(name: String) = flow {
        val trackerCore = TrackerClickLinkCore.create(name)
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

    override fun trackFilters(filters: List<String>) = flow {
        val trackerCore = TrackerFilterCore.create(filters)
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
        val trackerCore = TrackerSortCore.create(sortType)
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

    override fun trackImpression(data: Any) = flow {
        val trackerCore = TrackerImpressionCore.create(data)
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
        status: Boolean,
        reason: String?
    ) = flow {
        val trackerCore = TrackerSubmissionCore.create(name, status, reason)
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
        val trackerCore = TrackerDisplayedItemCore.create(data)
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
        keyword: String
    ) = flow {
        val trackerCore = TrackerSearchCore.create(keyword = keyword)
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