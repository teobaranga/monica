package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class ContactActivityEditRoute(
    val contactId: Int,
    val activityId: Int?,
)

fun NavGraphBuilder.contactActivityEdit() {
    composable<ContactActivityEditRoute> {
        EditContactActivity()
    }
}
