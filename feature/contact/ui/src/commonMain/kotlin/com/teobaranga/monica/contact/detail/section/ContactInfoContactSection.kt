package com.teobaranga.monica.contact.detail.section

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data object ContactInfoContactSection : ContactInfoSection {

    override val title: String = "Contact"

    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            modifier = modifier,
        ) {
            // TODO
        }
    }
}
