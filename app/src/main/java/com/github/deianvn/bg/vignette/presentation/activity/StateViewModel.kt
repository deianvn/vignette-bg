package com.github.deianvn.bg.vignette.presentation.activity

import androidx.lifecycle.ViewModel
import com.github.deianvn.bg.vignette.state.Plot
import com.github.deianvn.bg.vignette.state.Scene
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.error.StateError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class StateViewModel<A, P : Plot<P>>(
    initialStatus: Status,
    initialAct: A,
    initialPlot: P,
    initialFault: StateError? = null
) : ViewModel() {

    private val _scene = MutableStateFlow<Scene<A, P>>(
        Scene(
            status = initialStatus,
            act = initialAct,
            plot = initialPlot,
            fault = initialFault
        )
    )

    val scene get() = _scene.asStateFlow()

    fun publish(action: (scene: Scene<A, P>) -> Scene<A, P>) {
        _scene.value = action(_scene.value)
    }

}
