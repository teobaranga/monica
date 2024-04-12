package com.teobaranga.monica.data.user

import androidx.room.Embedded
import androidx.room.Relation
import com.teobaranga.monica.contacts.data.ContactEntity

data class MeFullDetails(
    @Embedded
    val info: MeEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "contactId",
    )
    val contact: ContactEntity?,
)
