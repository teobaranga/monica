package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data object ContactInfoWorkSection : ContactInfoSection {

    override val title: String = "Work"

    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            modifier = modifier,
        ) {
            // TODO
        }
    }
}
