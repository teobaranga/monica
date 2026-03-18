package com.teobaranga.monica.contact.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val contactId: Int,
    val firstName: String,
    val lastName: String?,
    val nickname: String?,
    /**
     * Complete name in the format "First Last (Nickname)".
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
    val avatar: ContactEntityAvatar,
    @Embedded(prefix = "birthdate_")
    val birthdate: ContactEntityBirthdate?,
    val genderId: Int?,
    val updated: Instant?,
    val syncStatus: SyncStatus,
)

data class ContactEntityAvatar(
    val url: String?,
    val color: String,
)

/**
 * note: [isAgeBased] is mutually exclusive with [isYearUnknown].
 */
data class ContactEntityBirthdate(
    val isAgeBased: Boolean,
    val isYearUnknown: Boolean,
    val date: LocalDate,
)
