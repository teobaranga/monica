package com.teobaranga.monica.activity.nav

import kotlinx.serialization.Serializable

@Serializable
data class ContactActivityEditRoute(
    val contactId: Int?,
    val activityId: Int?,
)
