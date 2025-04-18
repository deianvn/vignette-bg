package com.github.deianvn.bg.vignette.state.act

import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.state.model.VignetteWidgetEntry


sealed class WidgetAct : Act()


sealed class WidgetConfigAct : WidgetAct()

class VignetteSelectionListAct(
    val vignettes: List<VignetteEntry> = emptyList()
) : WidgetConfigAct()

class VignetteSaveSelectionAct(
    val widgetId: Int = 0,
    val serializedVignette: String? = null
) : WidgetConfigAct()


sealed class WidgetUpdateAct : WidgetAct()

class RetrieveWidgetEntries(
    entries: List<VignetteWidgetEntry> = emptyList()
) : WidgetUpdateAct()
