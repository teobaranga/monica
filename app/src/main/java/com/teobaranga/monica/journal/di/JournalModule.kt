package com.teobaranga.monica.journal.di

import com.teobaranga.monica.account.AccountListener
import com.teobaranga.monica.journal.data.JournalEntrySynchronizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class JournalModule {

    @Binds
    @IntoSet
    abstract fun bindJournalSynchronizer(synchronizer: JournalEntrySynchronizer): AccountListener
}
