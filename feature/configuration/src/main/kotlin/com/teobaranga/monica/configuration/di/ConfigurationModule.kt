package com.teobaranga.monica.configuration.di

import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigurationModule {

    @Binds
    internal abstract fun bindsConfigurationDataStore(impl: ConfigurationDataStoreImpl): ConfigurationDataStore
}
