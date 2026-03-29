package com.teobaranga.monica.activity.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.teobaranga.monica.contact.data.local.ContactEntity

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
