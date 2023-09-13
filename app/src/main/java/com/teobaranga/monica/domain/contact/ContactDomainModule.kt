package com.teobaranga.monica.domain.contact

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactDomainModule {

    @Binds
    abstract fun bindGetContactAvatar(getContactAvatar: MonicaGetContactAvatar): GetContactAvatar
}
