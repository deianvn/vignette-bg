package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.deianvn.bg.vignette.state.model.VignetteEntry


@Composable
fun VignetteListSelectionTile(
    modifier: Modifier = Modifier,
    vignetteEntries: List<VignetteEntry>,
    onVignetteSelected: (entry: VignetteEntry) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(vignetteEntries) { entry ->
            VignetteTile(
                modifier = Modifier.clickable(
                    onClick = { onVignetteSelected(entry) }
                ),
                vignetteEntry = entry
            )
        }
    }
}
