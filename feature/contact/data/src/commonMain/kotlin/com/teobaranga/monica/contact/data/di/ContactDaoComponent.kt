package com.teobaranga.monica.contact.data.di

import com.teobaranga.monica.contact.data.local.ContactDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface ContactDaoComponent {


    @Provides
    fun providesContactDao(owner: ContactTableOwner): ContactDao = owner.contactDao()
}
