package com.teobaranga.monica.contacts.di

import com.teobaranga.monica.account.AccountListener
import com.teobaranga.monica.contacts.data.ContactSynchronizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactsModule {

    @Binds
    @IntoSet
    abstract fun bindContactSynchronizer(synchronizer: ContactSynchronizer): AccountListener
}
