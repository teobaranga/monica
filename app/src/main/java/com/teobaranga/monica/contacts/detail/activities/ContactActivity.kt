package com.teobaranga.monica.contacts.detail.activities

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.activities.data.ContactActivityEntity
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.toExternalModel
import com.teobaranga.monica.contacts.list.model.Contact

@Immutable
data class ContactActivity(
    val id: Int,
    val title: String,
    val description: String?,
    val date: String,
    val participants: List<Contact>,
)

fun ContactActivityEntity.toExternalModel(participants: List<ContactEntity>): ContactActivity {
    return ContactActivity(
        id = activityId,
        title = title,
        description = description,
        date = date,
        participants = participants.map { it.toExternalModel() },
    )
}
