package com.github.deianvn.bg.vignette.state.repository

import android.content.Context
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.state.model.Country
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import com.github.deianvn.bg.vignette.utils.readResourceAsString
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.reflect.Type

class CountryRepository(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val moshi: Moshi
) {

    private val countryListType: Type = Types.newParameterizedType(
        List::class.java,
        Country::class.java
    )

    suspend fun retrieveCountries(): List<Country> = withContext(
        dispatcherProvider.io
    ) retrieveCountries@{
        val json = context.readResourceAsString(R.raw.countries)
        val countries = moshi.adapter<List<Country>>(countryListType).fromJson(json)

        if (countries == null) {
            Timber.e("Countries resource not available.")
        }

        return@retrieveCountries countries ?: emptyList()
    }

}
