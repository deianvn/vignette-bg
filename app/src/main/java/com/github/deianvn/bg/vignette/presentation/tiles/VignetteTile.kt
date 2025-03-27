package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.state.model.Vignette
import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.utils.ValidityUnit
import com.github.deianvn.bg.vignette.utils.createValidityMessage


@Composable
fun VignetteTile(
    vignetteEntry: VignetteEntry,
    modifier: Modifier = Modifier
) {
    val vignette = vignetteEntry.vignette
    Box(modifier = modifier) {
        if (vignette == null || vignette.isExpired()) {
            InactiveVignette(vignetteEntry.countryCode, vignetteEntry.plate)
        } else {
            ActiveVignette(vignette)
        }
    }
}

@Composable
private fun InactiveVignette(countryCode: String, plate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(18.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = plate,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = stringResource(R.string.inactive),
            color = Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ActiveVignette(vignette: Vignette) {

    val validityMessage = createValidityMessage(vignette.validToDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(18.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = vignette.plate,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = vignette.validToDate.toString("dd.MM.yyyy - HH:mm"),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.left),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = String.format(
                    "%s %s",
                    validityMessage.quantity,
                    pluralStringResource(
                        when (validityMessage.unit) {
                            ValidityUnit.YEAR -> R.plurals.year_plural
                            ValidityUnit.MONTH -> R.plurals.month_plural
                            ValidityUnit.DAY -> R.plurals.day_plural
                            ValidityUnit.HOUR -> R.plurals.hour_plural
                            ValidityUnit.MINUTE -> R.plurals.minute_plural
                        },
                        validityMessage.quantity
                    )
                ),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
