package id.co.edtslib.tracker

import android.app.Application
import id.co.edtslib.tracker.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class Tracker private constructor(): KoinComponent {
    private val trackerViewModel: TrackerViewModel? by inject()

    companion object {
        private var tracker: Tracker? = null
        var baseUrl = ""
        var token = ""
        var debugging = false

        fun init(application: Application, baseUrl: String, token: String) {
            Tracker.baseUrl = baseUrl
            Tracker.token = token
            startKoin {
                androidContext(application.applicationContext)
                modules(
                    listOf(
                        networkingModule,
                        sharedPreferencesModule,
                        mainAppModule,
                        repositoryModule,
                        interactorModule,
                        viewModule
                    )
                )
            }

            if (tracker == null) {
                tracker = Tracker()
            }
            tracker?.trackerViewModel?.createSession()?.observeForever {
                tracker?.trackerViewModel?.trackStartApplication()?.observeForever {  }
            }
        }

        fun init(baseUrl: String, token: String, koin: KoinApplication) {
            Tracker.baseUrl = baseUrl
            Tracker.token = token

            koin.modules(listOf(
                networkingModule,
                sharedPreferencesModule,
                mainAppModule,
                repositoryModule,
                interactorModule,
                viewModule
            ))

            if (tracker == null) {
                tracker = Tracker()
            }
            tracker?.trackerViewModel?.createSession()?.observeForever {
                tracker?.trackerViewModel?.trackStartApplication()?.observeForever {  }
            }
        }

        fun setUserId(userId: Long) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.setUserId(userId)?.observeForever {  }
        }

        fun trackExitApplication() {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackExitApplication()?.observeForever {  }
        }


        fun trackPage(screenName: String) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackPage(screenName)?.observeForever {  }
        }

        fun trackPageDetail(name: String, detail: Any?) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackPageDetail(name, detail)?.observeForever {  }
        }

        fun trackClick(name: String) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackClick(name)?.observeForever {  }
        }

        fun trackFilters(name: String, filters: List<String>) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackFilters(name, filters)?.observeForever {  }

        }

        fun trackSort(name: String, sortType: String) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSort(name, sortType)?.observeForever {  }
        }

        fun trackSubmissionSuccess(name: String) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSubmission(name, true, "")?.observeForever {  }

        }

        fun trackSubmissionFailed(name: String, reason: String?) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSubmission(name, false, reason)?.observeForever {  }

        }

        fun trackImpression(name: String, data: Any) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackImpression(name, data)?.observeForever {  }

        }
    }
}