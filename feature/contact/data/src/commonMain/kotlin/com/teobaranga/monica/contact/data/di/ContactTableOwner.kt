package com.teobaranga.monica.contact.data.di

import com.teobaranga.monica.contact.data.local.ContactDao

interface ContactTableOwner {

    fun contactDao(): ContactDao
}
