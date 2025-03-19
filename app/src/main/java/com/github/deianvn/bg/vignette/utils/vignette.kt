package com.github.deianvn.bg.vignette.utils

import org.joda.time.LocalDateTime


enum class ValidityUnit {
    YEAR,
    MONTH,
    DAY,
    HOUR,
    MINUTE
}

data class ValidityMessage(
    val quantity: Int,
    val unit: ValidityUnit
)

fun createValidityMessage(
    validTo: LocalDateTime
): ValidityMessage {
    val now = LocalDateTime.now()
    return when {
        validTo < now -> ValidityMessage(0, ValidityUnit.MINUTE)

        validTo.year > now.year -> ValidityMessage(validTo.year - now.year, ValidityUnit.YEAR)

        validTo.monthOfYear > now.monthOfYear -> ValidityMessage(
            validTo.monthOfYear - now.monthOfYear,
            ValidityUnit.MONTH
        )

        validTo.dayOfMonth > now.dayOfMonth -> ValidityMessage(
            validTo.dayOfMonth - now.dayOfMonth,
            ValidityUnit.DAY
        )

        validTo.hourOfDay > now.hourOfDay -> ValidityMessage(
            validTo.hourOfDay - now.hourOfDay,
            ValidityUnit.HOUR
        )

        else -> ValidityMessage(
            validTo.minuteOfHour - now.minuteOfHour,
            ValidityUnit.MINUTE
        )
    }
}
