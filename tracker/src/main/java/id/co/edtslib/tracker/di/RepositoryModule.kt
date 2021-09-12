package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.TrackerRemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single { TrackerRemoteDataSource(get()) }
    single { TrackerLocalDataSource(get(), get()) }
    single { ConfigurationLocalSource(get(), get()) }

    single<ITrackerRepository> { TrackerRepository(get(), get(), get()) }
}