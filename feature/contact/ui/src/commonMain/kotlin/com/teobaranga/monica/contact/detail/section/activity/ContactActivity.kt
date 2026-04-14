package com.teobaranga.monica.contact.detail.section.activity

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
