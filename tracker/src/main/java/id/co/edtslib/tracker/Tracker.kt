package id.co.edtslib.tracker

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import id.co.edtslib.tracker.data.InstallReferer
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
        var resend = true

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

        fun checkInstallReferrer(activity: FragmentActivity) {
            val referrerClient = InstallReferrerClient.newBuilder(activity).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            checkInstallReferrer(referrerClient.installReferrer?.installReferrer!!,
                                activity.intent)
                        }
                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            // API not available on the current Play Store app.
                        }
                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            // Connection couldn't be established.
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }

        fun checkInstallReferrer(utm_raw: String?, intent: Intent?) {
            if (intent?.data?.getQueryParameter("utm_source") != null) {
                tracker?.trackerViewModel?.setInstallReferer(InstallReferer(intent.data?.toString()))
            }
            else {

                tracker?.trackerViewModel?.setInstallReferer(InstallReferer(utm_raw))
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