package com.github.deianvn.bg.vignette.presentation.tiles

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.state.model.Vignette
import com.github.deianvn.bg.vignette.state.model.VignetteEntry
import com.github.deianvn.bg.vignette.utils.ValidityUnit
import com.github.deianvn.bg.vignette.utils.createValidityMessage


@Composable
fun VignetteWidgetTile(
    vignetteEntry: VignetteEntry?,
    modifier: GlanceModifier = GlanceModifier
) {
    val vignette = vignetteEntry?.vignette
    Box(modifier = modifier) {
        if (vignette == null || vignette.isExpired()) {
            if (vignetteEntry != null) {
                InactiveVignette(vignetteEntry.countryCode, vignetteEntry.plate)
            } else {
                Box(modifier = GlanceModifier.fillMaxSize().background(Color.Red)) {
                }
            }
        } else {
            ActiveVignette(vignette)
        }
    }
}

@Composable
private fun InactiveVignette(countryCode: String, plate: String) {
    val context = LocalContext.current

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text(
            text = plate
        )

        Text(
            text = context.getString(R.string.inactive)
        )
    }
}

@Composable
private fun ActiveVignette(vignette: Vignette) {

    val context = LocalContext.current

    val validityMessage = createValidityMessage(vignette.validToDate)

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = vignette.plate
            )
            Text(
                text = vignette.validToDate.toString("dd.MM.yyyy - HH:mm")
            )
        }

        Spacer(modifier = GlanceModifier.width(16.dp))

        Column(
            modifier = GlanceModifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = context.getString(R.string.left)
            )
            Spacer(modifier = GlanceModifier.height(12.dp))
            Text(
                text = String.format(
                    "%s %s",
                    validityMessage.quantity,
                    context.applicationContext.resources.getQuantityString(
                        when (validityMessage.unit) {
                            ValidityUnit.YEAR -> R.plurals.year_plural
                            ValidityUnit.MONTH -> R.plurals.month_plural
                            ValidityUnit.DAY -> R.plurals.day_plural
                            ValidityUnit.HOUR -> R.plurals.hour_plural
                            ValidityUnit.MINUTE -> R.plurals.minute_plural
                        },
                        validityMessage.quantity
                    )
                )
            )
        }
    }
}
