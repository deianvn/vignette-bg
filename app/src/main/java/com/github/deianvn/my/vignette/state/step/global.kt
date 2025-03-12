package com.github.deianvn.my.vignette.state.step

import com.github.deianvn.my.vignette.state.model.Country
import com.github.deianvn.my.vignette.state.model.VignetteConfig

sealed class Step

class LoadPrerequisites : Step()

class PrerequisitesLoaded(
    val countries: List<Country>,
    val vignetteConfigs: List<VignetteConfig>
) : Step()

class LoadVignette