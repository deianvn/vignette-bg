package com.github.deianvn.bg.vignette.utils

import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.joda.time.Months
import org.joda.time.Years


val SOFIA_TIME_ZONE: DateTimeZone = DateTimeZone.forID("Europe/Sofia")


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
    val now = LocalDateTime.now(SOFIA_TIME_ZONE)

    if (validTo < now) return ValidityMessage(0, ValidityUnit.MINUTE)

    val yearsLeft = Years.yearsBetween(now, validTo).years
    val monthsLeft = Months.monthsBetween(now, validTo).months
    val daysLeft = Days.daysBetween(now, validTo).days
    val hoursLeft = Hours.hoursBetween(now, validTo).hours

    return when {
        yearsLeft > 0 -> ValidityMessage(yearsLeft, ValidityUnit.YEAR)
        monthsLeft > 0 -> ValidityMessage(monthsLeft, ValidityUnit.MONTH)
        daysLeft > 0 -> ValidityMessage(daysLeft, ValidityUnit.DAY)
        hoursLeft > 0 -> ValidityMessage(hoursLeft, ValidityUnit.HOUR)
        else -> ValidityMessage(Minutes.minutesBetween(now, validTo).minutes, ValidityUnit.MINUTE)
    }
}
