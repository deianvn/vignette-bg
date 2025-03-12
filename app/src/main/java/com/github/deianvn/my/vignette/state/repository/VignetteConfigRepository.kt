package com.github.deianvn.my.vignette.state.repository

import com.github.deianvn.my.vignette.state.model.VignetteConfig
import com.github.deianvn.my.vignette.utils.coroutines.DispatcherProvider
import com.github.deianvn.my.vignette.utils.preferences.SharedPrefStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.withContext

private const val KEY_VIGNETTE_CONFIGS = "vignetteConfigs"

class VignetteConfigRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val sharedPrefStorage: SharedPrefStorage,
    private val moshi: Moshi
) {

    private val listType = Types.newParameterizedType(
        List::class.java, VignetteConfig::class.java
    )

    suspend fun storeVignetteConfigs(vignetteConfigs: List<VignetteConfig>) = withContext(
        dispatcherProvider.io
    ) {
        val json = moshi.adapter(List::class.java).toJson(vignetteConfigs)
        sharedPrefStorage.putString(KEY_VIGNETTE_CONFIGS, json)
    }

    suspend fun retrieveVignetteConfigs(): List<VignetteConfig>? = withContext(
        dispatcherProvider.io
    ) {
        val json = sharedPrefStorage
            .getString(KEY_VIGNETTE_CONFIGS) ?: return@withContext emptyList()

        return@withContext moshi.adapter<List<VignetteConfig>>(listType).fromJson(json)
            ?: emptyList()
    }

}
