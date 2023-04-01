package com.github.deianvn.my.vignette.di

import com.github.deianvn.my.vignette.main.MainActivityViewModel
import com.github.deianvn.my.vignette.repository.CountryRepository
import com.github.deianvn.my.vignette.repository.ValidityRepository
import com.github.deianvn.my.vignette.repository.VignetteConfigRepository
import com.github.deianvn.my.vignette.repository.VignetteRepository
import org.koin.androidx.viewmodel.dsl.viewModel
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
        MainActivityViewModel(get(), get(), get(), get())
    }

}
