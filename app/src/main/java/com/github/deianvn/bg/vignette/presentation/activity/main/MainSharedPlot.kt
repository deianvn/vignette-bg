package com.github.deianvn.bg.vignette.presentation.activity.main

import com.github.deianvn.bg.vignette.state.Plot
import com.github.deianvn.bg.vignette.state.model.Country
import com.github.deianvn.bg.vignette.state.model.VignetteEntry

data class MainSharedPlot(
    var countries: List<Country> = emptyList(),
    var countriesMap: Map<String, Country> = emptyMap(),
    var vignetteEntries: List<VignetteEntry> = emptyList()
) : Plot<MainSharedPlot> {

    override fun copyObject() = copy()

}
