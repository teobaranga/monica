package com.teobaranga.monica.activity.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class ContactActivityEditRoute(
    val contactId: Int?,
    val activityId: Int?,
)

fun NavGraphBuilder.contactActivityEdit() {
    composable<ContactActivityEditRoute> {
//        EditContactActivity()
    }
}
