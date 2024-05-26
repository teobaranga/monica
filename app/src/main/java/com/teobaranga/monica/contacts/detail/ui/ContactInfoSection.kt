package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface ContactInfoSection {
    val title: String

    @Composable
    fun Content(modifier: Modifier, navigator: DestinationsNavigator)
}
