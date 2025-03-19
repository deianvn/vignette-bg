package com.github.deianvn.bg.vignette.state.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Country(
    val code: String,
    val name: String
)
