package com.teobaranga.monica.contacts.detail.bio.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun GenderItem(gender: String, modifier: Modifier = Modifier) {
    BioItem(
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Outlined.PersonOutline,
                contentDescription = null,
            )
        },
        title = "Gender",
        description = gender,
    )
}

@Preview
@Composable
private fun PreviewGenderItem() {
    MonicaTheme {
        GenderItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            gender = "Male",
        )
    }
}
