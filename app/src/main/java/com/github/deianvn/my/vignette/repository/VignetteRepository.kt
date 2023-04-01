package com.github.deianvn.my.vignette.repository

import com.github.deianvn.my.vignette.api.BgTollApi
import com.github.deianvn.my.vignette.dto.Vignette
import io.reactivex.rxjava3.core.Single

class VignetteRepository(
    private val bgTollApi: BgTollApi
) {

    fun requestVignette(countryCode: String, plate: String): Single<Vignette> {
        return bgTollApi.getVignette(countryCode, plate)
            .map { it.vignette ?: throw NoVignetteAvailableException() }
    }

}
