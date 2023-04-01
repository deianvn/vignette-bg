package com.github.deianvn.my.vignette.repository

import com.github.deianvn.my.vignette.model.VignetteConfig
import com.github.deianvn.my.vignette.preferences.SharedPrefStorage
import io.reactivex.rxjava3.core.Single

private const val KEY_COUNTRY_CODE = "countryCode"
private const val KEY_PLATE = "plate"

class VignetteConfigRepository(
    private val sharedPrefStorage: SharedPrefStorage
) {

    fun storeConfig(countryCode: String, plate: String) {
        sharedPrefStorage.putString(KEY_COUNTRY_CODE, countryCode)
        sharedPrefStorage.putString(KEY_PLATE, plate)
    }

    fun retrieveConfig(): Single<VignetteConfig> {
        val countryCode = sharedPrefStorage.getString(KEY_COUNTRY_CODE)
        val plate = sharedPrefStorage.getString(KEY_PLATE)
        if (countryCode != null && plate != null) {
            return Single.fromCallable { VignetteConfig(countryCode, plate) }
        }
        return Single.error(NoVignetteConfigException())
    }

}
