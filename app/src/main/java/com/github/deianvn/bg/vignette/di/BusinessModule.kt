package com.github.deianvn.bg.vignette.di

import com.github.deianvn.bg.vignette.presentation.activity.main.MainViewModel
import com.github.deianvn.bg.vignette.state.repository.CountryRepository
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.utils.preferences.SharedPrefStorage
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

    viewModel {
        MainViewModel(get(), get())
    }

}
