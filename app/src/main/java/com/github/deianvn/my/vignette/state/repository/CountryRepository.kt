package com.github.deianvn.my.vignette.state.repository

import com.github.deianvn.my.vignette.state.model.Country
import com.github.deianvn.my.vignette.utils.coroutines.DispatcherProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class CountryRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val moshi: Moshi
) {

    private val countryListType: Type = Types.newParameterizedType(
        List::class.java,
        Country::class.java
    )

    suspend fun retrieveCountries(): List<Country> = withContext(
        dispatcherProvider.io
    ) {

    }

}
