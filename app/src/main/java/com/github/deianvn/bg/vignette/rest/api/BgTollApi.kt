package com.github.deianvn.bg.vignette.rest.api

import com.github.deianvn.bg.vignette.rest.dto.VignetteResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface BgTollApi {

    @GET("/check/vignette/plate/{countryCode}/{plate}")
    suspend fun getVignette(
        @Path(value = "countryCode") countryCode: String,
        @Path(value = "plate") plate: String
    ): VignetteResponseDTO
}
