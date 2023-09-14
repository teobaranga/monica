package com.teobaranga.monica.data.user

import androidx.room.Embedded
import androidx.room.Relation
import com.teobaranga.monica.data.contact.ContactEntity
import com.teobaranga.monica.data.photo.PhotoEntity

data class MeFullDetails(
    @Embedded
    val info: MeEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "id",
    )
    val contact: ContactEntity?,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "contactId",
    )
    val photos: List<PhotoEntity>,
)
