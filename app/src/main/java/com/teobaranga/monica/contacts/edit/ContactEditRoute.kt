package com.teobaranga.monica.contacts.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class ContactEditRoute(
    val contactId: Int? = null,
    val contactName: String? = null,
)

fun NavGraphBuilder.contactEdit() {
    composable<ContactEditRoute> { backStackEntry ->
        ContactEdit()
    }
}
