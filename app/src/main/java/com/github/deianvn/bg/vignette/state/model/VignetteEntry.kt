package com.github.deianvn.bg.vignette.state.model

data class VignetteEntry(
    val countryCode: String,
    val plate: String,
    var vignette: Vignette? = null
)
