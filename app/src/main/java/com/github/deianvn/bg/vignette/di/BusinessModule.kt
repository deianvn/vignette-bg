package com.github.deianvn.bg.vignette.di

import com.github.deianvn.bg.vignette.presentation.activity.main.MainViewModel
import com.github.deianvn.bg.vignette.presentation.activity.widget.VignetteWidgetConfigurationViewModel
import com.github.deianvn.bg.vignette.state.repository.CountryRepository
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.state.repository.WidgetRepository
import com.github.deianvn.bg.vignette.utils.storage.SharedPrefStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val BusinessModule = module {

    single {
        SharedPrefStorage(androidApplication())
    }

    single {
        VignetteRepository(get(), get(), get(), get())
    }

    single {
        CountryRepository(androidApplication(), get(), get())
    }

    single {
        WidgetRepository(get(), get(), get())
    }

    viewModel {
        MainViewModel(get(), get())
    }

    viewModel {
        VignetteWidgetConfigurationViewModel(get(), get())
    }

}
