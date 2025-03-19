package com.github.deianvn.bg.vignette.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.deianvn.bg.vignette.R


data class SearchableDropDownOption(
    val id: String,
    val label: String
)

@Composable
fun SearchableDropDown(
    modifier: Modifier = Modifier,
    options: List<SearchableDropDownOption>,
    label: String = "",
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    // Filter the options based on search query
    val filteredOptions = options.filter {
        it.label.contains(searchQuery, ignoreCase = true) || it.id.startsWith(
            searchQuery,
            ignoreCase = true
        )
    }

    Column(modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            enabled = false,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        if (expanded) {
            Dialog(
                onDismissRequest = { expanded = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(text = stringResource(R.string.seearch_country)) },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        enabled = true,
                        trailingIcon = null
                    )

                    if (filteredOptions.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(R.string.no_countries_found)
                        )
                    } else {
                        LazyColumn {
                            items(filteredOptions) { option ->
                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),

                                    onClick = {
                                        selectedOption = option.label
                                        onOptionSelected(option.id)
                                        expanded = false
                                        searchQuery = ""
                                    }
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = option.label,
                                        textAlign = TextAlign.Start,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
