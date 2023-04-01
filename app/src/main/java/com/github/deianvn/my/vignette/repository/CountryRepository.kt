package com.github.deianvn.my.vignette.repository

import android.content.Context
import com.github.deianvn.my.vignette.R
import com.github.deianvn.my.vignette.model.Country
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single
import okio.*
import java.lang.reflect.Type

class CountryRepository(
    private val moshi: Moshi
) {

    private val countryListType: Type = Types.newParameterizedType(
        List::class.java,
        Country::class.java
    )

    fun getCountries(context: Context): Single<List<Country>> {

        return Single.fromCallable {
            moshi.adapter<List<Country>>(countryListType).fromJson(
                context.resources.openRawResource(R.raw.countries)
                    .source()
                    .buffer()
            )
        }
    }

}