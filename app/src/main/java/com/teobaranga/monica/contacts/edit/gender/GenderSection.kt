package com.teobaranga.monica.contacts.edit.gender

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.teobaranga.monica.genders.domain.Gender
import com.teobaranga.monica.ui.text.MonicaTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSection(
    gender: Gender?,
    onGenderChange: (Gender) -> Unit,
    genders: List<Gender>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            modifier = Modifier
                .padding(top = 12.dp),
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            },
        ) {
            MonicaTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                state = TextFieldState(gender?.name.orEmpty()),
                readOnly = true,
                lineLimits = TextFieldLineLimits.SingleLine,
            )
            DropdownMenu(
                modifier = Modifier
                    .exposedDropdownSize(true)
                    .heightIn(max = 260.dp),
                properties = PopupProperties(focusable = false),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = gender.name,
                            )
                        },
                        onClick = {
                            onGenderChange(gender)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
