package com.teobaranga.monica.genders.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenderApiModule {

    @[Provides Singleton]
    internal fun provideApi(retrofit: Retrofit): GendersApi {
        return retrofit.create(GendersApi::class.java)
    }
}
