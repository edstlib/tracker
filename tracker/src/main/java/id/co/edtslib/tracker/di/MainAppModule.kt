package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.TrackerApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val mainAppModule = module {
    single { provideTrackerApiService(get(named("tracker"))) }
}

private fun provideTrackerApiService(retrofit: Retrofit): TrackerApiService =
    retrofit.create(TrackerApiService::class.java)