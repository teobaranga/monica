package com.teobaranga.monica.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {

    @Binds
    abstract fun bindsAuthorizationRepository(repository: MonicaAuthorizationRepository): AuthorizationRepository
}
