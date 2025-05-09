package com.teobaranga.monica.journal.data.local

interface JournalDatabaseOwner {

    fun journalDao(): JournalDao
}
