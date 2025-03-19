package com.github.deianvn.bg.vignette.presentation.dispatcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.deianvn.bg.vignette.presentation.activity.main.MainViewModel
import com.github.deianvn.bg.vignette.presentation.page.LoadingDataPage
import com.github.deianvn.bg.vignette.presentation.page.VignetteListPage
import com.github.deianvn.bg.vignette.state.step.Prerequisites
import com.github.deianvn.bg.vignette.state.step.VignetteList
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainDispatcher(
    viewModel: MainViewModel = koinViewModel()
) {
    val scene by viewModel.scene.collectAsStateWithLifecycle()
    val plot = scene.plotCopy
    val act = scene.act

    when (act) {
        is Prerequisites -> LoadingDataPage()
        is VignetteList -> VignetteListPage(
            vignetteEntries = scene.plotCopy.vignetteEntries,
            plot.countries,
            act.isNewVignetteRequested,
            onNewVignetteRequested = { viewModel.newVignetteRequested() },
            onNewVignetteCanceled = { viewModel.newVignetteCanceled() },
            onNewVignetteSubmitted = { countryCode, plate -> viewModel.addVignette(countryCode, plate) },
            onVignetteDismissed = { viewModel.removeVignetteEntry(it) }
        )
    }
}
