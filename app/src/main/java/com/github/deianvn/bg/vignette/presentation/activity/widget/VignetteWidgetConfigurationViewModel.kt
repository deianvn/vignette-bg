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
import kotlinx.coroutines.launch
import timber.log.Timber


class VignetteWidgetConfigurationViewModel(
    private val vignetteRepository: VignetteRepository,
    private val widgetRepository: WidgetRepository
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

            publish {
                it.next(
                    status = Status.SUCCESS,
                    act = VignetteSaveSelectionAct(widgetId)
                )
            }
        }
    }

}
