package com.teobaranga.monica.activity.data

import androidx.room3.Embedded
import androidx.room3.Junction
import androidx.room3.Relation
import com.teobaranga.monica.contact.data.local.ContactEntity

data class ContactActivityWithParticipants(
    @Embedded
    val activity: ContactActivityEntity,
    @Relation(
        parentColumns = ["activityId"],
        entityColumns = ["contactId"],
        associateBy = Junction(ContactActivityCrossRef::class),
    )
    val participants: List<ContactEntity>,
)
