package com.teobaranga.monica.activity.edit.domain

import com.teobaranga.monica.activity.edit.ui.ActivityParticipant
import kotlinx.datetime.LocalDate

data class Activity(
    val summary: String,
    val details: String?,
    val date: LocalDate,
    val participants: List<ActivityParticipant.Contact>,
)
