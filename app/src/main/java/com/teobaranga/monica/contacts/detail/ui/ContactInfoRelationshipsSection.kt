package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data object ContactInfoRelationshipsSection : ContactInfoSection {

    override val title: String = "Relationships"

    @Composable
    override fun Content(modifier: Modifier, navigator: DestinationsNavigator) {
        Column(
            modifier = modifier,
        ) {
            // TODO
        }
    }
}
