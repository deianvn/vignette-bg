package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.deianvn.bg.vignette.R

@Composable
fun Toolbar(
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_flag_bg),
            tint = Color.Unspecified,
            contentDescription = stringResource(R.string.bulgarian_flag_icon_desc)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
