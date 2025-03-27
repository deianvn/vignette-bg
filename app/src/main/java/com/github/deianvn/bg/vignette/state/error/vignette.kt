package com.github.deianvn.bg.vignette.state.error

sealed class VignetteError(message: String? = null) : IllegalStateException(
    message ?: "Vignette error"
)

class VignetteNotAvailableError(country: String, plate: String) : VignetteError(
    "Vignette not available for country $country and plate $plate"
)
