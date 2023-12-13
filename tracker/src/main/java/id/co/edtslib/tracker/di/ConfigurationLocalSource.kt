package id.co.edtslib.tracker.di

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.co.edtslib.tracker.data.Configuration

class ConfigurationLocalSource(sharedPreferences: SharedPreferences, app: Application): LocalDataSource<Configuration>(sharedPreferences) {
    private var configuration: Configuration? = null

    override fun getKeyName(): String = "trackerConfiguration"
    override fun getValue(json: String): Configuration = Gson().fromJson(json, object : TypeToken<Configuration>() {}.type)

    override fun save(data: Configuration?) {
        configuration = data
        super.save(data)
    }

    override fun getCached(): Configuration? {
        return if (configuration != null) {
            configuration
        } else {
            super.getCached()
        }
    }

    override fun clear() {
        configuration = null
        super.clear()
    }

    fun getSessionId() = getCached()?.sessionId
    fun getUserId(): Long {
        val configuration = getCached()
        return if (configuration?.userId == null) 0L else configuration.userId
    }

    fun getLatitude(): Double? {
        val configuration = getCached()
        return configuration?.latitude
    }

    fun getLongitude(): Double? {
        val configuration = getCached()
        return configuration?.longitude
    }

    fun getPreviousPageName(): String? {
        val configuration = getCached()
        return configuration?.previousPageName
    }

    fun setPreviousPageName(pageName: String) {
        var configuration = getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId = "",
                userId = 0)
        }
        configuration.previousPageName = pageName
        save(configuration)
    }

    fun getPrevPageUrlPath(): String? {
        val configuration = getCached()
        return configuration?.prevPageUrlPath
    }

    fun setPrevPageUrlPath(pageUrlPath: String) {
        var configuration = getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId = "",
                userId = 0)
        }
        configuration.prevPageUrlPath = pageUrlPath
        save(configuration)
    }

    fun getEventId(): Long {
        var configuration = getCached()
        if (configuration == null) {
            configuration = Configuration(sessionId = "",
                userId = 0)
            configuration.eventId = 0
        }
        configuration.eventId++
        save(configuration)

        return configuration.eventId
    }

}