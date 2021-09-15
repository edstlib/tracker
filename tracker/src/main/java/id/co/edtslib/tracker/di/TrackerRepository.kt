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
    private val installReferrer: InstallReferer? = null
    override fun createSession() = flow {
        val sessionId = String.format("%s-%d", UUID.randomUUID().toString(),
            Date().time)
        var configuration = configurationLocalSource.getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId, 0L)
        }
        else {
            configuration.sessionId = sessionId
        }
        configurationLocalSource.save(configuration)

        emit(sessionId)
    }

    override fun setUserId(userId: Long) = flow {
        val configuration = configurationLocalSource.getCached()
        if (configuration != null) {
            configuration.userId = userId
        }
        configurationLocalSource.save(configuration)
        emit(userId)
    }

    override fun trackStartApplication() = flow {
        val trackerCore = TrackerActivityCore.createPageResume()
        val trackerData = TrackerData(trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer)

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
        val trackerData = TrackerData(trackerCore
            , TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId())
            , localSource.apps, installReferrer)

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

    override fun trackPage(name: String) = flow {
        val trackerCore = TrackerPageViewCore.create(name)
        val trackerData = TrackerData(
            trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer
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

    override fun trackPageDetail(name: String, detail: Any?) = flow {
        val trackerCore = TrackerPageDetailCore.create(name, detail)
        val trackerData = TrackerData(trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer)

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
        val trackerData = TrackerData(trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer)

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

    override fun trackFilters(name: String, filters: List<String>) = flow {
        val trackerCore = TrackerFilterCore.create(name, filters)
        val trackerData = TrackerData(trackerCore
            , TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId())
            , localSource.apps, installReferrer)

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

    override fun trackSort(name: String, sortType: String) = flow {
        val trackerCore = TrackerSortCore.create(name, sortType)
        val trackerData = TrackerData(trackerCore
            , TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId())
            , localSource.apps, installReferrer)

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

    override fun trackImpression(name: String, data: Any) = flow {
        val trackerCore = TrackerImpressionCore.create(name, data)
        val trackerData = TrackerData(trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer)

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
        val trackerData = TrackerData(trackerCore,
            TrackerUser.create(configurationLocalSource.getSessionId(),
                configurationLocalSource.getUserId()),
            localSource.apps, installReferrer)

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