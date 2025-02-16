package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.activities.data.ContactActivityWithParticipants
import com.teobaranga.monica.contacts.data.toExternalModel
import com.teobaranga.monica.contacts.list.model.Contact
import java.time.LocalDate
import kotlin.uuid.Uuid

@Immutable
data class ContactActivity(
    val id: Int,
    val uuid: Uuid,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val participants: List<Contact>,
)

fun ContactActivityWithParticipants.toExternalModel(contactId: Int): ContactActivity {
    return ContactActivity(
        id = activity.activityId,
        uuid = activity.uuid,
        title = activity.title,
        description = activity.description,
        date = activity.date,
        participants = participants
            .filterNot { it.contactId == contactId }
            .map { it.toExternalModel() },
    )
}
