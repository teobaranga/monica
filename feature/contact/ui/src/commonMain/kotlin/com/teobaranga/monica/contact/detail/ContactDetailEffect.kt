package com.teobaranga.monica.contact.detail

sealed interface ContactDetailEffect {
    object Deleted : ContactDetailEffect
}
