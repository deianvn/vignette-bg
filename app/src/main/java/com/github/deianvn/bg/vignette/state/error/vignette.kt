package com.github.deianvn.bg.vignette.state.error

sealed class VignetteError(val country: String, val plate: String) : StateError()

class VignetteNotAvailableError(country: String, plate: String) : VignetteError(
    country = country, plate = plate
)

class VignetteEntryNotAvailable(country: String, plate: String) : VignetteError(
    country = country, plate = plate
)
