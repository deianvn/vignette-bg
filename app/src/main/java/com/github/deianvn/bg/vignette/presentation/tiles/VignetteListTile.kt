package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.presentation.components.SwipeToDismissItem
import com.github.deianvn.bg.vignette.state.model.VignetteEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VignetteListTile(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    vignetteEntries: List<VignetteEntry>,
    onVignetteDismissed: (VignetteEntry) -> Unit = {},
    onRefreshRequested: () -> Unit = {}
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { onRefreshRequested() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(vignetteEntries) { entry ->
                    SwipeToDismissItem(
                        item = { VignetteTile(entry) },
                        onDismissed = {
                            onVignetteDismissed(entry)
                        }
                    )
                }
            }
        }
    }
}
