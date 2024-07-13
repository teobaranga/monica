package com.teobaranga.monica.contacts.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.contacts.list.model.Contact
import com.teobaranga.monica.contacts.list.userAvatar
import com.teobaranga.monica.data.sync.SyncStatus
import java.time.OffsetDateTime

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val contactId: Int,
    val firstName: String,
    val lastName: String?,
    val nickname: String?,
    /**
     * Complete name in the format "First Middle Last (Nickname)".
     * Server-driven.
     */
    val completeName: String,
    /**
     * Initials of the contact.
     * Server-driven.
     */
    val initials: String,
    /**
     * Avatar of the user.
     * Server-driven.
     */
    @Embedded(prefix = "avatar_")
    val avatar: Avatar,
    @Embedded(prefix = "birthdate_")
    val birthdate: Birthdate?,
    val updated: OffsetDateTime?,
    val syncStatus: SyncStatus,
) {
    data class Avatar(
        val url: String?,
        val color: String,
    )
    data class Birthdate(
        val isAgeBased: Boolean,
        val isYearUnknown: Boolean,
        val date: OffsetDateTime,
    )
}

fun ContactEntity.toExternalModel(): Contact {
    return Contact(
        id = contactId,
        firstName = firstName,
        lastName = lastName,
        completeName = completeName,
        initials = run {
            if (initials.length >= 2) {
                initials
            } else if (!lastName.isNullOrBlank()) {
                firstName.take(1) + lastName.take(1)
            } else {
                firstName.take(2)
            }
        },
        avatar = userAvatar,
        updated = updated,
    )
}
