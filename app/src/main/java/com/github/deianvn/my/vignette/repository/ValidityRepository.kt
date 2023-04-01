package com.github.deianvn.my.vignette.repository

import com.github.deianvn.my.vignette.preferences.SharedPrefStorage
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.joda.time.LocalDateTime

private const val KEY_VALIDITY = "validity"

class ValidityRepository(
    private val sharedPrefStorage: SharedPrefStorage,
    moshi: Moshi
) {

    private val localDateTimeAdapter = moshi.adapter(LocalDateTime::class.java)

    fun getValidity(): Single<LocalDateTime> {
        val validityStr = sharedPrefStorage.getString(KEY_VALIDITY)
            ?: throw NoValidityAvailableException()

        return Single.fromCallable {
            localDateTimeAdapter.fromJson(validityStr)
        }
    }

    fun storeValidity(validity: LocalDateTime): Completable {
        return Completable.fromCallable {
            sharedPrefStorage.putString(
                KEY_VALIDITY, localDateTimeAdapter.toJson(validity)
            )
        }
    }

    fun clearValidity(): Completable {
        return Completable.fromCallable {
            sharedPrefStorage.clear(KEY_VALIDITY)
        }
    }

}
