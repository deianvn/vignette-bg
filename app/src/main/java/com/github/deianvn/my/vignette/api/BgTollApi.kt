package com.github.deianvn.my.vignette.api

import com.github.deianvn.my.vignette.dto.VignetteResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface BgTollApi {

    @GET("/check/vignette/plate/{countryCode}/{plate}")
    fun getVignette(
        @Path(value = "countryCode") countryCode: String,
        @Path(value = "plate") plate: String
    ): Single<VignetteResponseDTO>
}
