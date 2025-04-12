package com.github.deianvn.bg.vignette.presentation.activity.main

import androidx.lifecycle.viewModelScope
import com.github.deianvn.bg.vignette.presentation.activity.StateViewModel
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.error.StateError
import com.github.deianvn.bg.vignette.state.error.VignetteNotAvailableError
import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.state.repository.CountryRepository
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.state.act.Act
import com.github.deianvn.bg.vignette.state.act.PrerequisitesAct
import com.github.deianvn.bg.vignette.state.act.VignetteListAct
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel(
    private val countryRepository: CountryRepository,
    private val vignetteRepository: VignetteRepository,
    private val sharedPlot: MainSharedPlot = MainSharedPlot()
) : StateViewModel<Act, MainSharedPlot>(
    Status.LOADING, PrerequisitesAct(), sharedPlot
) {

    init {

        viewModelScope.launch {
            try {
                publish {
                    it.next(
                        status = Status.LOADING,
                        act = PrerequisitesAct()
                    )
                }

                loadPrerequisites()

                publish {
                    it.next(
                        status = Status.LOADING,
                        act = VignetteListAct()
                    )
                }

                loadVignettes()

                publish { it.next(status = Status.SUCCESS) }
            } catch (e: StateError) {
                Timber.e(e, "Could not load prerequisites.")
                publish { it.next(status = Status.ERROR, fault = e) }
            }
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

    @Throws(StateError::class)
    private suspend fun loadVignettes(): Boolean = coroutineScope {

        val vignetteEntries = vignetteRepository.retrieveVignetteEntries()
        var apiCallPerformed = false

        vignetteEntries.forEach { entry ->
            val vignette = entry.vignette
            if (vignette == null || vignette.isExpired()) {
                val vignette = try {
                    apiCallPerformed = true
                    vignetteRepository.requestVignette(
                        countryCode = entry.countryCode,
                        plate = entry.plate
                    )
                } catch (_: VignetteNotAvailableError) {
                    Timber.w("vignette not available with countryCode, plate: ${entry.countryCode}, ${entry.plate}")
                    null
                } catch (e: StateError) {
                    throw e
                }
                entry.vignette = vignette
            }
        }

        vignetteRepository.storeVignetteEntries(vignetteEntries)
        sharedPlot.vignetteEntries = vignetteEntries

        return@coroutineScope apiCallPerformed

    }

    fun newVignetteRequested() {
        publish {
            it.next(
                status = Status.SUCCESS,
                act = VignetteListAct(true)
            )
        }
    }

    fun newVignetteCanceled() {
        publish {
            it.next(
                status = Status.SUCCESS,
                act = VignetteListAct()
            )
        }
    }

    fun addVignette(countryCode: String, plate: String) {
        publish { it.next(status = Status.LOADING) }

        viewModelScope.launch {

            val vignetteExists = sharedPlot.vignetteEntries.any {
                it.countryCode == countryCode && it.plate == plate
            }

            if (vignetteExists) {
                publish {
                    it.next(
                        status = Status.SUCCESS,
                        act = VignetteListAct()
                    )
                }
            } else {

                try {
                    vignetteRepository.storeVignetteEntries(
                        sharedPlot.vignetteEntries + VignetteEntry(countryCode, plate)
                    )

                    loadPrerequisites()
                    loadVignettes()

                    publish {
                        it.next(
                            status = Status.SUCCESS,
                            act = VignetteListAct()
                        )
                    }
                } catch (e: StateError) {
                    Timber.e(e, "Could not add vignette.")
                    publish { it.next(status = Status.ERROR, fault = e) }
                }
            }
        }
    }

    fun removeVignetteEntry(vignette: VignetteEntry) {
        publish { it.next(status = Status.LOADING) }

        viewModelScope.launch {
            vignetteRepository.storeVignetteEntries(
                sharedPlot.vignetteEntries.filter {
                    it.countryCode == vignette.countryCode && it.plate != vignette.plate
                }
            )

            try {
                loadVignettes()

                publish {
                    it.next(
                        status = Status.SUCCESS,
                        act = VignetteListAct()
                    )
                }
            } catch (e: StateError) {
                Timber.e(e, "Could not load vignettes when removing.")
                publish {
                    it.next(status = Status.ERROR, fault = e)
                }
            }
        }
    }

    fun refreshVignettes() {
        Timber.i("Refresh vignettes")
        publish {
            it.next(
                status = Status.SUCCESS,
                act = VignetteListAct(isRefreshing = true)
            )
        }

        viewModelScope.launch {
            try {
                if (!loadVignettes()) {
                    delay(300L)
                }

                publish {
                    it.next(
                        status = Status.SUCCESS,
                        act = VignetteListAct()
                    )
                }
            } catch (e: StateError) {
                Timber.e(e, "Could not load vignettes when refreshing.")

                publish {
                    it.next(
                        status = Status.ERROR,
                        act = VignetteListAct(),
                        fault = e
                    )
                }
            }
        }
    }

}
