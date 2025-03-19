package com.github.deianvn.bg.vignette

import com.github.deianvn.bg.vignette.di.BusinessModule
import com.github.deianvn.bg.vignette.di.CoroutineModule
import com.github.deianvn.bg.vignette.di.RetrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@Application)
            modules(
                listOf(CoroutineModule, RetrofitModule, BusinessModule)
            )
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}
