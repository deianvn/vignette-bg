package com.github.deianvn.bg.vignette.rest.serialization

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class MoshiLocalDateTimeAdapter {

    companion object {
        private val PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

    @ToJson
    fun toJson(value: LocalDateTime) = value.toString(PATTERN)

    @FromJson
    fun fromJson(json: String): LocalDateTime = LocalDateTime.parse(json, PATTERN)
}
