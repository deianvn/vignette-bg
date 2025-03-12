package com.github.deianvn.my.vignette.di

import com.github.deianvn.my.vignette.presentation.activity.main.MainViewModel
import com.github.deianvn.my.vignette.state.repository.CountryRepository
import com.github.deianvn.my.vignette.state.repository.ValidityRepository
import com.github.deianvn.my.vignette.state.repository.VignetteConfigRepository
import com.github.deianvn.my.vignette.state.repository.VignetteRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val BusinessModule = module {

    single {
        VignetteRepository(get())
    }

    single {
        CountryRepository(get())
    }

    single {
        ValidityRepository(get(), get())
    }

    single {
        VignetteConfigRepository(get())
    }

    viewModel {
        MainViewModel(get(), get(), get(), get())
    }

}
