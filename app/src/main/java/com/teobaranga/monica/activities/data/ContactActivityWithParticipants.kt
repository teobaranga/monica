package com.teobaranga.monica.activities.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.teobaranga.monica.contacts.data.ContactEntity

data class ContactActivityWithParticipants(
    @Embedded
    val activity: ContactActivityEntity,
    @Relation(
        parentColumn = "activityId",
        entityColumn = "contactId",
        associateBy = Junction(ContactActivityCrossRef::class),
    )
    val participants: List<ContactEntity>,
)
