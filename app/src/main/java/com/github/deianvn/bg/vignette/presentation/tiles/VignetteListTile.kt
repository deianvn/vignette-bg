package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.presentation.components.SwipeToDismissItem
import com.github.deianvn.bg.vignette.state.model.VignetteEntry


@Composable
fun VignetteListTile(
    vignetteEntries: List<VignetteEntry>,
    modifier: Modifier = Modifier,
    onVignetteDismissed: (VignetteEntry) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {

        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = stringResource(R.string.vignettes),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(vignetteEntries) { entry ->
                SwipeToDismissItem(
                    item = { VignetteTile(entry.vignette) },
                    onDismissed = {
                        onVignetteDismissed(entry)
                    }
                )
            }
        }
    }
}
