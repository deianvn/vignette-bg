package com.github.deianvn.bg.vignette.presentation.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.deianvn.bg.vignette.state.Plot
import com.github.deianvn.bg.vignette.state.Scene
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.error.VignetteNotAvailableError
import com.github.deianvn.bg.vignette.state.model.Country
import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.state.repository.CountryRepository
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.state.step.Act
import com.github.deianvn.bg.vignette.state.step.Prerequisites
import com.github.deianvn.bg.vignette.state.step.VignetteList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val countryRepository: CountryRepository,
    private val vignetteRepository: VignetteRepository
) : ViewModel() {

    data class SharedPlot(
        var countries: List<Country> = emptyList(),
        var countriesMap: Map<String, Country> = emptyMap(),
        var vignetteEntries: List<VignetteEntry> = emptyList()
    ) : Plot<SharedPlot> {
        override fun copyObject() = copy()
    }

    private val sharedPlot = SharedPlot()

    private val _scene = MutableStateFlow(
        Scene<Act, SharedPlot>(act = Prerequisites(), status = Status.LOADING, plot = sharedPlot)
    )

    val scene get() = _scene.asStateFlow()

    init {
        viewModelScope.launch {
            _scene.value = _scene.value.next(
                status = Status.LOADING,
                act = Prerequisites()
            )

            loadPrerequisites()

            _scene.value = _scene.value.next(
                status = Status.LOADING,
                act = VignetteList()
            )

            loadVignettes()

            _scene.value = _scene.value.success()
        }
    }

    private suspend fun loadPrerequisites() = coroutineScope {
        val loadCountriesDeferred = async { loadCountries() }
        loadCountriesDeferred.await()
    }

    private suspend fun loadCountries() {
        val countries = countryRepository.retrieveCountries()

        sharedPlot.countries = countries
        sharedPlot.countriesMap = countries.associateBy { it.code }
    }

    private suspend fun loadVignettes() = coroutineScope {

        val vignetteEntries = vignetteRepository.retrieveVignetteEntries()

        vignetteEntries.forEach { entry ->
            val vignette = entry.vignette
            if (vignette == null || vignette.isExpired()) {
                val vignette = try {
                    vignetteRepository.requestVignette(
                        countryCode = entry.countryCode,
                        plate = entry.plate
                    )
                } catch (e: VignetteNotAvailableError) {
                    null
                } catch (e: Exception) {
                    throw e
                }
                entry.vignette = vignette
            }
        }

        vignetteRepository.storeVignetteEntries(vignetteEntries)
        sharedPlot.vignetteEntries = vignetteEntries
    }

    fun newVignetteRequested() {
        _scene.value = _scene.value.next(
            status = Status.SUCCESS,
            act = VignetteList(true)
        )
    }

    fun newVignetteCanceled() {
        _scene.value = _scene.value.next(
            status = Status.SUCCESS,
            act = VignetteList()
        )
    }

    fun addVignette(countryCode: String, plate: String) {
        _scene.value = _scene.value.loading()

        viewModelScope.launch {

            val vignetteExists = sharedPlot.vignetteEntries.any {
                it.countryCode == countryCode && it.plate == plate
            }

            if (vignetteExists) {
                //TODO send error
                _scene.value = _scene.value.next(
                    status = Status.SUCCESS,
                    act = VignetteList()
                )
            } else {

                vignetteRepository.storeVignetteEntries(
                    sharedPlot.vignetteEntries + VignetteEntry(countryCode, plate)
                )

                loadPrerequisites()
                loadVignettes()

                _scene.value = _scene.value.next(
                    status = Status.SUCCESS,
                    act = VignetteList()
                )
            }
        }
    }

    fun removeVignetteEntry(vignette: VignetteEntry) {
        _scene.value = _scene.value.loading()

        viewModelScope.launch {
            vignetteRepository.storeVignetteEntries(
                sharedPlot.vignetteEntries.filter {
                    it.countryCode == vignette.countryCode && it.plate != vignette.plate
                }
            )

            loadVignettes()

            _scene.value = _scene.value.next(
                status = Status.SUCCESS,
                act = VignetteList()
            )
        }
    }

}
