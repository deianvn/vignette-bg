package com.github.deianvn.bg.vignette.presentation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun VignetteListLayout(
    listComponent: @Composable () -> Unit,
    buttonComponent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            listComponent()
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-24).dp, (-24).dp)
        ) {
            buttonComponent()
        }
    }
}
