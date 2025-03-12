package com.github.deianvn.my.vignette.rest.dto

data class VignetteResponseDTO(
    val vignette: VignetteDTO?,
    val ok: Boolean,
    val status: VignetteResponseStatus
)