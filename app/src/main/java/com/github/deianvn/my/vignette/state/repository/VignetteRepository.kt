package com.github.deianvn.my.vignette.state.repository

import com.github.deianvn.my.vignette.rest.api.BgTollApi
import com.github.deianvn.my.vignette.rest.dto.VignetteDTO
import io.reactivex.rxjava3.core.Single

class VignetteRepository(
    private val bgTollApi: BgTollApi
) {

    fun requestVignette(countryCode: String, plate: String): Single<VignetteDTO> {
        return bgTollApi.getVignette(countryCode, plate)
            .map { it.vignette ?: throw NoVignetteAvailableException() }
    }

}
