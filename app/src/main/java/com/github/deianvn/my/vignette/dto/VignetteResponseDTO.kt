package com.github.deianvn.my.vignette.dto

data class VignetteResponseDTO(
    val vignette: Vignette?,
    val ok: Boolean,
    val status: VignetteResponseStatus
)