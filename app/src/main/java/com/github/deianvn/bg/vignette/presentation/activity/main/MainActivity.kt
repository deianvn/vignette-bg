package com.github.deianvn.bg.vignette.presentation.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.presentation.page.LoadingDataPage
import com.github.deianvn.bg.vignette.presentation.page.VignetteListPage
import com.github.deianvn.bg.vignette.presentation.tiles.Toolbar
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.step.Prerequisites
import com.github.deianvn.bg.vignette.state.step.VignetteList
import getErrorMessageResource
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Screen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Screen() {

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Toolbar() }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Content(snackbarHostState)
            }
        }

    }

}


@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scene by viewModel.scene.collectAsStateWithLifecycle()
    val plot = scene.plotCopy
    val act = scene.act

    when (scene.status) {
        Status.LOADING -> LoadingDataPage()
        Status.ERROR -> {
            LaunchedEffect(scene) {
                val result = snackbarHostState.showSnackbar(
                    message = context.getString(getErrorMessageResource(scene.fault)),
                    withDismissAction = false,
                    actionLabel = context.getString(R.string.retry)
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.refreshVignettes()
                }
            }
        }
        Status.SUCCESS -> when (act) {
            is Prerequisites -> LoadingDataPage()
            is VignetteList -> VignetteListPage(
                vignetteEntries = scene.plotCopy.vignetteEntries,
                plot.countries,
                act.isNewVignetteRequested,
                onNewVignetteRequested = { viewModel.newVignetteRequested() },
                onNewVignetteCanceled = { viewModel.newVignetteCanceled() },
                onNewVignetteSubmitted = { countryCode, plate ->
                    viewModel.addVignette(
                        countryCode,
                        plate
                    )
                },
                onVignetteDismissed = { viewModel.removeVignetteEntry(it) },
                onRefreshRequested = { viewModel.refreshVignettes() }
            )
        }
    }
}
