package com.github.deianvn.my.vignette.state.model

import org.joda.time.LocalDateTime

data class Vignette(
    val country: Country,
    val plate: String,
    val expireDate: LocalDateTime
)
