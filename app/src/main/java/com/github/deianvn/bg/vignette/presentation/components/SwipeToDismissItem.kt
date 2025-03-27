package com.github.deianvn.bg.vignette.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.deianvn.bg.vignette.R
import kotlin.math.roundToInt

@Composable
fun SwipeToDismissItem(item: @Composable () -> Unit, onDismissed: () -> Unit) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var itemWidth by remember { mutableFloatStateOf(1f) }
    var itemHeightDp by remember { mutableFloatStateOf(1f) }
    val autoDismissThreshold = itemWidth * 0.4f
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Red)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.delete_icon_desc),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    itemWidth = it.width.toFloat()
                    itemHeightDp = it.height.toFloat() / density
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = (offsetX + dragAmount).coerceAtLeast(0f)
                            offsetX = newOffset.coerceAtMost(itemWidth)
                        },
                        onDragEnd = {
                            when {
                                offsetX >= autoDismissThreshold -> {
                                    onDismissed()
                                }

                                else -> {
                                    offsetX = 0f
                                }
                            }
                        },
                        onDragCancel = {
                            offsetX = 0f
                        }
                    )
                }
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .background(Color.White)
        ) {
            item()
        }
    }
}
