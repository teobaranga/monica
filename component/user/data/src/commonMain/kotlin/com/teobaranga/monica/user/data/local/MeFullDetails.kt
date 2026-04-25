package com.teobaranga.monica.user.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.teobaranga.monica.contact.data.local.ContactEntity

data class MeFullDetails(
    @Embedded
    val info: MeEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "contactId",
    )
    val contact: ContactEntity?,
)
