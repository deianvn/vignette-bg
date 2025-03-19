package com.github.deianvn.bg.vignette.rest.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VignetteResponseStatus(
    val code: Int,
    val message: String
)
