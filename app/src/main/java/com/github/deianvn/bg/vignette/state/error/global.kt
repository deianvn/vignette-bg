package com.github.deianvn.bg.vignette.state.error

class VignetteNotAvailableError(country: String, plate: String) : IllegalStateException(
    "Vignette not available for country $country and plate $plate"
)
