package com.github.deianvn.bg.vignette.state.repository

import com.github.deianvn.bg.vignette.state.model.VignetteWidgetEntry
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import com.github.deianvn.bg.vignette.utils.storage.SharedPrefStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext


private const val KEY_VIGNETTE_WIDGET_ENTRIES = "vignette_widget_entries"

class WidgetRepository(
    private val sharedPrefStorage: SharedPrefStorage,
    private val dispatcherProvider: DispatcherProvider,
    private val moshi: Moshi
) {

    private val listType = Types.newParameterizedType(
        List::class.java, VignetteWidgetEntry::class.java
    )

    private val _entriesState: MutableStateFlow<List<VignetteWidgetEntry>> = MutableStateFlow(
        emptyList()
    )
    val entriesState: StateFlow<List<VignetteWidgetEntry>> = _entriesState


    suspend fun addVignetteWidgetEntry(vignetteWidgetEntry: VignetteWidgetEntry) = withContext(
        dispatcherProvider.io
    ) {
        if (entriesState.value.isEmpty()) {
            retrieveVignetteWidgetEntries()
        }

        val entries = entriesState.value.toMutableList()
        entries.add(vignetteWidgetEntry)
        storeVignetteWidgetEntries(entries)
        _entriesState.value = entries
    }

    suspend fun removeVignetteWidgetEntry(widgetId: Int) = withContext(dispatcherProvider.io) {
        if (entriesState.value.isEmpty()) {
            retrieveVignetteWidgetEntries()
        }
        val entries = entriesState.value.toMutableList()
        entries.removeIf { it.widgetId == widgetId }
        storeVignetteWidgetEntries(entries)
        _entriesState.value = entries
    }

    suspend fun storeVignetteWidgetEntries(entries: List<VignetteWidgetEntry>) = withContext(
        dispatcherProvider.io
    ) {
        val json = moshi.adapter(List::class.java).toJson(entries)
        sharedPrefStorage.putString(KEY_VIGNETTE_WIDGET_ENTRIES, json)
    }

    suspend fun retrieveVignetteWidgetEntries() = withContext(
        dispatcherProvider.io
    ) {
        val json =
            sharedPrefStorage.getString(KEY_VIGNETTE_WIDGET_ENTRIES)

        val entries =
            if (json != null) moshi.adapter<List<VignetteWidgetEntry>>(listType).fromJson(json)
                ?: emptyList()
            else emptyList()

        _entriesState.value = entries
    }

}
