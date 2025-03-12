package com.github.deianvn.my.vignette.di

import com.github.deianvn.my.vignette.rest.api.BgTollApi
import com.github.deianvn.my.vignette.utils.interceptor.DebugRetrofitInterceptor
import com.github.deianvn.my.vignette.utils.moshi.MoshiLocalDateTimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val RetrofitModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(DebugRetrofitInterceptor())
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Moshi.Builder()
            .add(MoshiLocalDateTimeAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://check.bgtoll.bg")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(BgTollApi::class.java)
    }

}
