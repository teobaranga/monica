package com.teobaranga.monica.activity.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ContactActivityEditRoute(
    val contactId: Int?,
    val activityId: Int?,
) : NavKey
