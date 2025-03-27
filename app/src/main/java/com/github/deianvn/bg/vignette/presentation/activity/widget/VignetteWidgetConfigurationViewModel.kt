package com.github.deianvn.bg.vignette.presentation.activity.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.deianvn.bg.vignette.state.repository.VignetteRepository
import kotlinx.coroutines.launch

class VignetteWidgetConfigurationViewModel(
    private val vignetteRepository: VignetteRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val vignetteEntries = vignetteRepository.retrieveVignetteEntries()
        }
    }

}
