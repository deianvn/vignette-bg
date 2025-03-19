package com.github.deianvn.bg.vignette.presentation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.presentation.components.SearchableDropDown
import com.github.deianvn.bg.vignette.presentation.components.SearchableDropDownOption
import com.github.deianvn.bg.vignette.state.model.Country


@Composable
fun NewVignetteDialog(
    countries: List<Country>,
    onAddClicked: (String, String) -> Unit = { _, _ -> },
    onDismissRequested: () -> Unit = {}
) {
    var selectedCountry by rememberSaveable { mutableStateOf<String>("") }
    var plate by rememberSaveable { mutableStateOf<String>("") }

    Dialog(
        onDismissRequest = onDismissRequested
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.add_new_vignette),
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                SearchableDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.country),
                    options = countries.map { SearchableDropDownOption(it.code, it.name) }
                ) {
                    selectedCountry = it
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text(text = stringResource(R.string.plate)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = true,
                    trailingIcon = null
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { onAddClicked(selectedCountry, plate) },
                    enabled = selectedCountry.isNotBlank() && plate.isNotBlank()
                ) {
                    Text(text = stringResource(R.string.add))
                }
            }
        }
    }
}
