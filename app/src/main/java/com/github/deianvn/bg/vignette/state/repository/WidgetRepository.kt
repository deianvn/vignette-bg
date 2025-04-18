package com.github.deianvn.bg.vignette.state.repository

import com.github.deianvn.bg.vignette.state.model.VignetteWidgetEntry
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import com.github.deianvn.bg.vignette.utils.storage.SharedPrefStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber


private const val KEY_VIGNETTE_WIDGET_ENTRIES = "vignette_widget_entries"

class WidgetRepository(
    private val sharedPrefStorage: SharedPrefStorage,
    private val dispatcherProvider: DispatcherProvider,
    private val moshi: Moshi
) {

    private val listType = Types.newParameterizedType(
        List::class.java, VignetteWidgetEntry::class.java
    )

    private val mutex = Mutex()

    suspend fun addVignetteWidgetEntry(
        vignetteWidgetEntry: VignetteWidgetEntry
    ): List<VignetteWidgetEntry> = withContext(dispatcherProvider.io) {
        mutex.withLock {
            val currentEntries = retrieveVignetteWidgetEntries().toMutableList()
            currentEntries.add(vignetteWidgetEntry)
            storeVignetteWidgetEntries(currentEntries)
            currentEntries
        }
    }

    suspend fun removeVignetteWidgetEntry(
        widgetId: Int
    ): List<VignetteWidgetEntry> = withContext(dispatcherProvider.io) {
        mutex.withLock {
            val currentEntries = retrieveVignetteWidgetEntries().toMutableList()
            currentEntries.removeIf { it.widgetId == widgetId }
            storeVignetteWidgetEntries(currentEntries)
            currentEntries
        }
    }

    private suspend fun storeVignetteWidgetEntries(
        entries: List<VignetteWidgetEntry>
    ) = withContext(dispatcherProvider.io) {
        mutex.withLock {
            try {
                val json = moshi.adapter(List::class.java).toJson(entries)
                sharedPrefStorage.putString(KEY_VIGNETTE_WIDGET_ENTRIES, json)
            } catch (e: Exception) {
                Timber.e(e, "Error storing entries.")
            }
        }
    }

    suspend fun retrieveVignetteWidgetEntries(): List<VignetteWidgetEntry> = withContext(
        dispatcherProvider.io
    ) {
        mutex.withLock {
            try {
                sharedPrefStorage.getObject(KEY_VIGNETTE_WIDGET_ENTRIES, List::class.java)
                val json = sharedPrefStorage.getString(KEY_VIGNETTE_WIDGET_ENTRIES)
                if (json != null) {
                    moshi.adapter<List<VignetteWidgetEntry>>(listType).fromJson(json) ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                Timber.e(e, "Error retrieving entries.")
                emptyList()
            }
        }
    }
}
