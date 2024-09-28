package com.teobaranga.monica.contacts.detail.activities.edit.domain

import com.teobaranga.monica.contacts.detail.activities.edit.ui.ActivityParticipant
import java.time.LocalDate

data class Activity(
    val summary: String,
    val details: String?,
    val date: LocalDate,
    val participants: List<ActivityParticipant.Contact>,
)
