package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data object ContactInfoContactSection : ContactInfoSection {

    override val title: String = "Contact"

    @Composable
    override fun Content(modifier: Modifier, navigator: DestinationsNavigator) {
        Column(
            modifier = modifier,
        ) {
            // TODO
        }
    }
}
