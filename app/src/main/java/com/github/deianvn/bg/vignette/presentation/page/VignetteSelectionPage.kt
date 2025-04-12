package com.github.deianvn.bg.vignette.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.deianvn.bg.vignette.presentation.tiles.VignetteListSelectionTile
import com.github.deianvn.bg.vignette.state.model.VignetteEntry


@Composable
fun VignetteSelectionPage(
    vignetteEntries: List<VignetteEntry>,
    onVignetteSelected: (entry: VignetteEntry) -> Unit = {}
) {
    VignetteListSelectionTile(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        vignetteEntries = vignetteEntries,
        onVignetteSelected = { entry ->
            onVignetteSelected(entry)
        }
    )
}
