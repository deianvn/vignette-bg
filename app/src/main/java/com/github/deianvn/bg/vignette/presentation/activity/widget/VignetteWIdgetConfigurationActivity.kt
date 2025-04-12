package com.github.deianvn.bg.vignette.presentation.activity.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.presentation.page.LoadingDataPage
import com.github.deianvn.bg.vignette.presentation.page.VignetteSelectionPage
import com.github.deianvn.bg.vignette.presentation.tiles.Toolbar
import com.github.deianvn.bg.vignette.presentation.widget.vignette.VignetteWidget
import com.github.deianvn.bg.vignette.state.Status
import com.github.deianvn.bg.vignette.state.act.VignetteSaveSelectionAct
import com.github.deianvn.bg.vignette.state.act.VignetteSelectionListAct
import getErrorMessageResource
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


class VignetteWidgetConfigurationActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Timber.w("Invalid widget id")
            finish()
            return
        }

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Toolbar(stringResource(R.string.vignettes)) }
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Content(widgetId, snackbarHostState)
                }
            }
        }
    }

    @Composable
    private fun Content(
        widgetId: Int,
        snackbarHostState: SnackbarHostState,
        viewModel: VignetteWidgetConfigurationViewModel = koinViewModel()
    ) {
        val context = LocalContext.current
        val scene by viewModel.scene.collectAsStateWithLifecycle()
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
                        viewModel.reload()
                    }
                }
            }

            Status.SUCCESS -> when (act) {
                is VignetteSelectionListAct -> VignetteSelectionPage(act.vignettes) { entry ->
                    viewModel.selectVignetteForWidget(widgetId, entry.countryCode, entry.plate)
                }

                is VignetteSaveSelectionAct -> {

                    val activity = LocalActivity.current

                    if (activity != null) {
                        LaunchedEffect(scene) {
                            val glanceId = GlanceAppWidgetManager(context)
                                .getGlanceIdBy(act.widgetId)
                            VignetteWidget().update(activity, glanceId)

                            activity.apply {
                                setResult(RESULT_OK, Intent().apply {
                                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, act.widgetId)
                                })
                                finish()
                            }
                        }
                    }
                }

                else -> {
                }
            }
        }
    }

}
