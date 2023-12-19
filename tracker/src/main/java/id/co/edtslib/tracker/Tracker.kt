package id.co.edtslib.tracker

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import id.co.edtslib.baserecyclerview.BaseRecyclerViewAdapter
import id.co.edtslib.baserecyclerview2.BaseRecyclerView2
import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerFilterDetail
import id.co.edtslib.tracker.data.TrackerData
import id.co.edtslib.tracker.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.util.Date

class Tracker private constructor(): KoinComponent {
    private val trackerViewModel: TrackerViewModel? by inject()

    data class ImpressionData (
        val data: List<Any>,
        val time: Long
    )

    companion object {
        private var tracker: Tracker? = null
        var baseUrl = ""
        var token = ""
        var debugging = false
        var resend = true
        var appVersion = "1.0.0"

        private var firstImpression = -1
        private var lastImpression = -1

        // don't set manual, set with resume fun
        var currentPageName = ""
        var currentPageId = ""

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
        }

        fun getInstallReferer() =  tracker?.trackerViewModel?.getInstallReferer()

        fun checkInstallReferrer(activity: FragmentActivity) {
            val referrerClient = InstallReferrerClient.newBuilder(activity).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                checkInstallReferrer(
                                    referrerClient.installReferrer?.installReferrer!!,
                                    activity.intent
                                )
                            }
                            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                // API not available on the current Play Store app.
                            }
                            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                                // Connection couldn't be established.
                            }
                        }
                    }
                    catch (ignore: RuntimeException) {

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
                tracker?.trackerViewModel?.setInstallReferer(InstallReferer(intent.data?.toString()))?.observeForever {  }
            }
            else {

                tracker?.trackerViewModel?.setInstallReferer(InstallReferer(utm_raw))?.observeForever {  }
            }
        }

        fun setUserId(userId: Long) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.setUserId(userId)?.observeForever {  }
        }

        fun setLatLng(lat: Double, lng: Double) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.setLatLng(lat, lng)?.observeForever {  }
        }

        fun trackPage(pageName: String, pageId: String, pageUrlPath: String = "") {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackPage(pageName, pageId, pageUrlPath)?.observeForever {  }
            resumePage(pageName, pageId)
        }

        fun trackPageDetail(detail: Any?) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackPageDetail(detail)?.observeForever {  }
        }

        fun trackClick(name: String, category: String? = null, url: String? = null, details: Any? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackClick(name, category, url, details)?.observeForever {  }
        }

        fun trackFilters(filters: List<TrackerFilterDetail>) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackFilters(filters)?.observeForever {  }

        }

        fun trackSort(sortType: String) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSort(sortType)?.observeForever {  }
        }

        fun trackSubmissionSuccess(name: String, category: String, details: Any? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSubmission(name, category, true, "", details)?.observeForever {  }

        }

        fun trackSubmissionFailed(name: String, category: String, reason: String?, details: Any? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSubmission(name, category, false, reason, details)?.observeForever {  }

        }

        fun trackImpression(category: String, data: List<*>, mapper: ((data: List<*>) -> List<Any>)? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackImpression(category, Date().time, data, mapper)?.observeForever {  }
        }

        fun trackImpression(category: String, time: Long, data: List<*>, mapper: ((data: List<*>) -> List<Any>)? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackImpression(category, time, data, mapper)?.observeForever {  }
        }

        fun trackDisplayedItems(data: MutableList<Any>) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackDisplayedItems(data)?.observeForever {  }
        }

        fun trackSearch(keyword: String, details: Any? = null) {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackSearch(keyword, details)?.observeForever {  }
        }

        fun trackOpenApplication() {
            tracker?.trackerViewModel?.createSession()?.observeForever {
                tracker?.trackerViewModel?.trackOpenApplication()?.observeForever {  }
            }
        }

        fun trackCloseApplication() {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackCloseApplication()?.observeForever {  }
        }

        fun trackResumeApplication() {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackResumeApplication()?.observeForever {  }
        }

        fun trackMinimizeApplication() {
            if (tracker == null) {
                tracker = Tracker()
            }

            tracker?.trackerViewModel?.trackMinimizeApplication()?.observeForever {  }
        }

        fun resumePage(pageName: String, pageId: String) {
            currentPageName = pageName
            currentPageId = pageId

        }

        fun getData(): TrackerData? {
            if (tracker == null) {
                tracker = Tracker()
            }

            return tracker?.trackerViewModel?.getData()
        }

        fun setImpressionRecyclerView(category: String, recyclerView: RecyclerView, mapper: ((data: List<*>) -> List<Any>)? = null) {
            firstImpression = -1
            lastImpression = -1

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (recyclerView.tag != null) {
                            if (recyclerView.tag is List<*>) {
                                val list = recyclerView.tag as List<*>
                                list.forEach {
                                    if (it is ImpressionData) {
                                        trackImpression(
                                            category = category,
                                            time = it.time,
                                            data = it.data,
                                            mapper = mapper
                                        )
                                    }
                                }

                            }
                        }
                        recyclerView.tag = null
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if ((recyclerView.layoutManager is LinearLayoutManager || recyclerView.layoutManager is StaggeredGridLayoutManager) && (recyclerView.adapter is BaseRecyclerViewAdapter<*, *> || recyclerView.adapter is BaseRecyclerView2)) {

                        val first: Int
                        val last: Int
                        if (recyclerView.layoutManager is LinearLayoutManager) {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            first = layoutManager.findFirstVisibleItemPosition()
                            last = layoutManager.findLastVisibleItemPosition()
                        }
                        else {
                            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                            first = layoutManager.findFirstVisibleItemPositions(null)[0]
                            last = layoutManager.findLastVisibleItemPositions(null)[0]
                        }


                        if (firstImpression != first && lastImpression != last) {

                            firstImpression = first
                            lastImpression = last


                            if (recyclerView.adapter is BaseRecyclerViewAdapter<*, *>) {
                                addImpression(
                                    recyclerView,
                                    first,
                                    last,
                                    recyclerView.adapter as BaseRecyclerViewAdapter<*, *>
                                )
                            } else
                                if (recyclerView.adapter is BaseRecyclerView2) {
                                    addImpression(
                                        recyclerView,
                                        first,
                                        last,
                                        recyclerView.adapter as BaseRecyclerView2
                                    )
                                }
                        }
                    }
                }
            })
        }

        private fun addImpression(recyclerView: RecyclerView, first: Int, end: Int, adapter: BaseRecyclerViewAdapter<*, *>) {
            val l = mutableListOf<Any>()
            for (i in first until end) {
                if (adapter.list[i] != null) {
                    l.add(adapter.list[i]!!)
                }
            }

            val newData = ImpressionData(data = l, time = Date().time)
            if (recyclerView.tag == null) {
                recyclerView.tag = listOf(newData)
            }
            else
                if (recyclerView.tag is List<*>) {
                    val list = (recyclerView.tag as List<ImpressionData>).toMutableList()
                    list.add(newData)

                    recyclerView.tag = list

                }
        }

        private fun addImpression(recyclerView: RecyclerView, first: Int, end: Int, adapter: BaseRecyclerView2) {
            val l = mutableListOf<Any>()
            for (i in first until end) {
                if (adapter.list[i].data != null) {
                    l.add(adapter.list[i].data!!)
                }
            }

            val newData = ImpressionData(data = l, time = Date().time)
            if (recyclerView.tag == null) {
                recyclerView.tag = listOf(newData)
            }
            else
                if (recyclerView.tag is List<*>) {
                    val list = (recyclerView.tag as List<ImpressionData>).toMutableList()
                    list.add(newData)

                    recyclerView.tag = list

                }
        }
    }

}