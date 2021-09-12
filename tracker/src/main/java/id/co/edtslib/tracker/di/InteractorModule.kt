package id.co.edtslib.tracker.di

import org.koin.dsl.module

val interactorModule = module {
    factory<TrackerUseCase> { TrackerInteractor(get()) }
}