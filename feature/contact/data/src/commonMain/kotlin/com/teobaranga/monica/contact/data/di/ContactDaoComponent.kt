package com.teobaranga.monica.contact.data.di

import com.teobaranga.monica.contact.data.local.ContactDao
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface ContactDaoComponent {


    @Provides
    fun providesContactDao(owner: ContactTableOwner): ContactDao = owner.contactDao()
}
