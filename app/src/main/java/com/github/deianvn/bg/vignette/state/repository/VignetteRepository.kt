package com.github.deianvn.bg.vignette.state.repository

import com.github.deianvn.bg.vignette.rest.api.BgTollApi
import com.github.deianvn.bg.vignette.state.error.StateError
import com.github.deianvn.bg.vignette.state.error.VignetteNotAvailableError
import com.github.deianvn.bg.vignette.state.model.Vignette
import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import com.github.deianvn.bg.vignette.utils.toStateError
import com.github.deianvn.bg.vignette.utils.storage.SharedPrefStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext


private const val KEY_VIGNETTE_CONFIGS = "vignetteConfigs"

class VignetteRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val sharedPrefStorage: SharedPrefStorage,
    private val bgTollApi: BgTollApi,
    private val moshi: Moshi
) {

    private val listType = Types.newParameterizedType(
        List::class.java, VignetteEntry::class.java
    )

    private val mutex = Mutex()

    suspend fun storeVignetteEntries(vignetteEntries: List<VignetteEntry>) = withContext(
        dispatcherProvider.io
    ) {
        mutex.withLock {
            val json = moshi.adapter(List::class.java).toJson(vignetteEntries)
            sharedPrefStorage.putString(KEY_VIGNETTE_CONFIGS, json)
        }
    }

    suspend fun retrieveVignetteEntries(): List<VignetteEntry> = withContext(
        dispatcherProvider.io
    ) {
        mutex.withLock {
            val json = sharedPrefStorage
                .getString(KEY_VIGNETTE_CONFIGS) ?: return@withContext emptyList()

            moshi.adapter<List<VignetteEntry>>(listType).fromJson(json)
                ?: emptyList()
        }
    }

    suspend fun getVignetteEntry(
        countryCode: String, plate: String
    ): VignetteEntry? = withContext(dispatcherProvider.io) {
        mutex.withLock {
            retrieveVignetteEntries().find {
                it.countryCode == countryCode && it.plate == plate
            }
        }
    }

    @Throws(StateError::class, VignetteNotAvailableError::class)
    suspend fun requestVignette(
        countryCode: String,
        plate: String
    ): Vignette {
        val dto = try {
            bgTollApi.getVignette(countryCode, plate)
        } catch (e: Exception) {
            throw toStateError(e)
        }

        val vignette = dto.vignette
        val error = VignetteNotAvailableError(countryCode, plate)

        if (vignette == null) {
            throw error
        }

        return Vignette(
            countryCode,
            plate,
            vignette.issueDate ?: throw error,
            vignette.validityDateFrom ?: throw error,
            vignette.validityDateTo ?: throw error
        )
    }

}
