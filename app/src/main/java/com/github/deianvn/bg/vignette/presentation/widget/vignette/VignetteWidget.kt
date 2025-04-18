package com.github.deianvn.bg.vignette.presentation.widget.vignette

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.fillMaxSize
import com.github.deianvn.bg.vignette.presentation.tiles.VignetteWidgetTile
import com.github.deianvn.bg.vignette.state.model.Vignette
import com.squareup.moshi.Moshi
import org.koin.core.context.GlobalContext


class VignetteWidget : GlanceAppWidget() {

    private val moshi: Moshi = GlobalContext.get().get()

    companion object {
        val keyVignette = stringPreferencesKey("vignette")
    }

    override suspend fun provideGlance(
        context: Context,
        glanceId: GlanceId
    ) {
        provideContent {

            val prefs = currentState<Preferences>()
            val json = prefs[keyVignette]
            val vignette = if (json != null) {
                try {
                    moshi.adapter(Vignette::class.java).fromJson(json)
                } catch (e: Exception) {
                    null
                }
            } else null

            VignetteWidgetTile(
                modifier = GlanceModifier.fillMaxSize(),
                vignette = vignette
            )
        }
    }

}
