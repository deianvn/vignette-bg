package com.github.deianvn.bg.vignette.presentation.widget.vignette

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.github.deianvn.bg.vignette.state.repository.WidgetRepository
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext


class VignetteWidgetReceiver : GlanceAppWidgetReceiver() {

    private val widgetRepository: WidgetRepository = GlobalContext.get().get()

    private val dispatcherProvider: DispatcherProvider = GlobalContext.get().get()

    override val glanceAppWidget = VignetteWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)

        CoroutineScope(dispatcherProvider.default).launch {
            appWidgetIds.forEach {
                widgetRepository.removeVignetteWidgetEntry(it)
            }
        }
    }

}
