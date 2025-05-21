package com.teobaranga.monica.contacts.detail

sealed interface ContactDetailEffect {
    object Deleted : ContactDetailEffect
}
