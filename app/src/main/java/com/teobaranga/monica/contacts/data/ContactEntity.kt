package com.teobaranga.monica.contacts.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.contacts.list.model.Contact
import java.time.ZonedDateTime

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val completeName: String,
    val initials: String,
    val avatarUrl: String?,
    val avatarColor: String,
    @Embedded(prefix = "birthdate_")
    val birthdate: Birthdate?,
    val updated: ZonedDateTime?,
) {
    data class Birthdate(
        val isAgeBased: Boolean,
        val isYearUnknown: Boolean,
        val date: ZonedDateTime,
    )
}

fun ContactEntity.toExternalModel(): Contact {
    return Contact(
        id = id,
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
        avatarUrl = avatarUrl,
        avatarColor = avatarColor,
        updated = updated,
    )
}
