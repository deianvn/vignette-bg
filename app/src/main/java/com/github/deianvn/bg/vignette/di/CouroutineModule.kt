package com.github.deianvn.bg.vignette.di

import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val CoroutineModule = module {

    single {
        object : DispatcherProvider {
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val ui: CoroutineDispatcher
                get() = Dispatchers.Main

        } as DispatcherProvider
    }

}
