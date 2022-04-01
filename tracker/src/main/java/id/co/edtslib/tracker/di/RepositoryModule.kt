package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.TrackerRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single { TrackerRemoteDataSource(get()) }
    single { TrackerLocalDataSource(get(named("trackerSharePref")), get()) }
    single { ConfigurationLocalSource(get(named("trackerSharePref")), get()) }

    single<ITrackerRepository> { TrackerRepository(get(), get(), get()) }
}