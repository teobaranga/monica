package com.teobaranga.monica.activity.list

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@Immutable
data class ContactActivity(
    val id: Int,
    val uuid: Uuid,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val participants: List<Participant>,
)
