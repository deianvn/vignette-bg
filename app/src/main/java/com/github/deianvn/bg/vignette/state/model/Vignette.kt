package com.github.deianvn.bg.vignette.state.model

import org.joda.time.LocalDateTime


data class Vignette(
    val countryCode: String,
    val plate: String,
    val issueDate: LocalDateTime,
    val validFromDate: LocalDateTime,
    val validToDate: LocalDateTime
) {
    fun isExpired() = LocalDateTime.now() > validToDate
}
