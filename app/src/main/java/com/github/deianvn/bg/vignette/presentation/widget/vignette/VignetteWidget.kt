package com.github.deianvn.bg.vignette.presentation.widget.vignette

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import com.github.deianvn.bg.vignette.presentation.tiles.VignetteWidgetTile
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.state.repository.WidgetRepository
import org.koin.core.context.GlobalContext
import timber.log.Timber

class VignetteWidget : GlanceAppWidget() {

    private val widgetRepository: WidgetRepository = GlobalContext.get().get()

    private val vignetteRepository: VignetteRepository = GlobalContext.get().get()


    override suspend fun provideGlance(
        context: Context,
        glanceId: GlanceId
    ) {

        val id = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)

        provideContent {

            val entriesState = widgetRepository.entriesState.collectAsState(initial = emptyList())
            val vignetteEntry = entriesState.value.find { it.widgetId == id }

             if (vignetteEntry != null) {
                vignetteRepository.getVignetteEntry(widgetEntry.countryCode, widgetEntry.plate)
            } else {
                Timber.i("Widget: Can not find widget entry: $id")
                null
            }

            if (vignetteEntry == null) {
                Timber.i("Widget: Could not find vignette entry: ${widgetEntry?.countryCode} ${widgetEntry?.plate}")
            }

            VignetteWidgetTile(
                modifier = GlanceModifier.fillMaxSize(),
                vignetteEntry = vignetteEntry
            )
        }
    }

}
