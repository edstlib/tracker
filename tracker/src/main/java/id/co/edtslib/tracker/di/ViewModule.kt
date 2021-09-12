package id.co.edtslib.tracker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { TrackerViewModel(get()) }
}