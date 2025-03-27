package com.github.deianvn.bg.vignette.state.model

import com.github.deianvn.bg.vignette.utils.SOFIA_TIME_ZONE
import org.joda.time.LocalDateTime


data class Vignette(
    val countryCode: String,
    val plate: String,
    val issueDate: LocalDateTime,
    val validFromDate: LocalDateTime,
    val validToDate: LocalDateTime
) {
    fun isExpired() = LocalDateTime.now(SOFIA_TIME_ZONE) > validToDate
}
