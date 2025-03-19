package com.github.deianvn.bg.vignette.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.deianvn.bg.vignette.presentation.dialog.NewVignetteDialog
import com.github.deianvn.bg.vignette.presentation.layout.VignetteListLayout
import com.github.deianvn.bg.vignette.presentation.tiles.VignetteListTile
import com.github.deianvn.bg.vignette.state.model.Country
import com.github.deianvn.bg.vignette.state.model.VignetteEntry

@Composable
fun VignetteListPage(
    vignetteEntries: List<VignetteEntry>,
    countries: List<Country>,
    showAddVignetteDialog: Boolean = false,
    onNewVignetteRequested: () -> Unit = {},
    onNewVignetteCanceled: () -> Unit = {},
    onNewVignetteSubmitted: (countryCode: String, plate: String) -> Unit = { _, _ -> },
    onVignetteDismissed: (VignetteEntry) -> Unit = {}
) {
    VignetteListLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        listComponent = {
            VignetteListTile(
                modifier = Modifier.fillMaxSize(),
                vignetteEntries = vignetteEntries,
                onVignetteDismissed = { onVignetteDismissed(it) }
            )
        },
        buttonComponent = {
            FloatingActionButton(
                onClick = { onNewVignetteRequested() },
            ) {
                Icon(Icons.Filled.Add, "Add Vignette")
            }
        }
    )

    if (showAddVignetteDialog) {
        NewVignetteDialog(
            countries,
            onAddClicked = { countryCode, plate -> onNewVignetteSubmitted(countryCode, plate) },
            onDismissRequested = { onNewVignetteCanceled() }
        )
    }
}
