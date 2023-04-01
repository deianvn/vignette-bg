package com.github.deianvn.my.vignette.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.joda.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Vignette(
    val licensePlateNumber: String?,
    val country: String?,
    val exempt: Boolean?,
    val vignetteNumber: String?,
    val vehicleClass: String?,
    val emissionsClass: String?,
    @Json(name = "validityDateFromFormated")
    val validityDateFromFormatted: String?,
    val validityDateFrom: String?,
    @Json(name = "validityDateToFormated")
    val validityDateToFormatted: String?,
    val validityDateTo: LocalDateTime?,
    @Json(name = "issueDateFormated")
    val issueDateFormatted: String?,
    val issueDate: String?,
    val price: Double?,
    val currency: String?,
    val status: String?,
    val whitelist: Boolean?,
    val vehicleType: String?,
    val vehicleClassCode: String?,
    val emissionsClassCode: String?,
    val vehicleTypeCode: String?,
    val statusBoolean: Boolean?
)
