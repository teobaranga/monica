package com.teobaranga.monica.data.user

import androidx.room3.Embedded
import androidx.room3.Relation
import com.teobaranga.monica.contact.data.local.ContactEntity

data class MeFullDetails(
    @Embedded
    val info: MeEntity,
    @Relation(
        parentColumns = ["contactId"],
        entityColumns = ["contactId"],
    )
    val contact: ContactEntity?,
)
