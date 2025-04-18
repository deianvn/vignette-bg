package com.github.deianvn.bg.vignette.presentation.activity.widget

import androidx.lifecycle.viewModelScope
import com.github.deianvn.bg.vignette.presentation.activity.StateViewModel
import com.github.deianvn.bg.vignette.state.EmptyPlot
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.model.VignetteWidgetEntry
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import com.github.deianvn.bg.vignette.state.repository.WidgetRepository
import com.github.deianvn.bg.vignette.state.act.Act
import com.github.deianvn.bg.vignette.state.act.VignetteSaveSelectionAct
import com.github.deianvn.bg.vignette.state.act.VignetteSelectionListAct
import com.github.deianvn.bg.vignette.state.error.StateError
import com.github.deianvn.bg.vignette.state.error.VignetteEntryNotAvailable
import com.github.deianvn.bg.vignette.state.error.VignetteNotAvailableError
import com.github.deianvn.bg.vignette.state.model.Vignette
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class VignetteWidgetConfigurationViewModel(
    private val vignetteRepository: VignetteRepository,
    private val widgetRepository: WidgetRepository,
    private val moshi: Moshi
) : StateViewModel<Act, EmptyPlot>(
    Status.LOADING, VignetteSelectionListAct(), EmptyPlot()
) {

    init {
        reload()
    }

    fun reload() {
        publish {
            it.next(
                status = Status.LOADING,
                act = VignetteSelectionListAct()
            )
        }

        viewModelScope.launch {
            val vignetteEntries = vignetteRepository.retrieveVignetteEntries()

            publish {
                it.next(
                    status = Status.SUCCESS,
                    act = VignetteSelectionListAct(vignetteEntries)
                )
            }
        }
    }

    fun selectVignetteForWidget(widgetId: Int, countryCode: String, plate: String) {
        viewModelScope.launch {
            publish {
                it.next(
                    status = Status.LOADING,
                    act = VignetteSaveSelectionAct()
                )
            }

            Timber.i("Selecting vignette for widget: $widgetId $countryCode $plate")

            widgetRepository.addVignetteWidgetEntry(
                VignetteWidgetEntry(widgetId = widgetId, countryCode = countryCode, plate = plate)
            )

            val vignetteEntries = vignetteRepository.retrieveVignetteEntries()
            val vignetteEntry = vignetteEntries.find {
                it.countryCode == countryCode && it.plate == plate
            }

            if (vignetteEntry == null) {
                publish {
                    it.next(
                        status = Status.ERROR,
                        fault = VignetteEntryNotAvailable(countryCode, plate)
                    )
                }

                return@launch
            }

            val vignette = try {
                vignetteRepository.requestVignette(countryCode, plate)
            } catch (e: StateError) {
                publish {
                    it.next(
                        status = Status.ERROR,
                        fault = e
                    )
                }

                return@launch
            }

            val serializedVignette = try {
                moshi.adapter(Vignette::class.java).toJson(vignette)
            } catch (_: Exception) {
                publish {
                    it.next(
                        status = Status.ERROR,
                        fault = VignetteNotAvailableError(countryCode, plate)
                    )
                }

                return@launch
            }

            vignetteEntry.vignette = vignette
            vignetteRepository.storeVignetteEntries(vignetteEntries)

            publish {
                it.next(
                    status = Status.SUCCESS,
                    act = VignetteSaveSelectionAct(
                        widgetId,
                        serializedVignette
                    )
                )
            }
        }
    }

}
