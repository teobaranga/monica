package com.teobaranga.monica.genders.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import com.r0adkll.kimchi.annotations.ContributesTo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenderApiModule {

    @[Provides Singleton]
    internal fun provideApi(retrofit: Retrofit): GendersApi {
        return retrofit.create(GendersApi::class.java)
    }
}

@ContributesTo(AppScope::class)
interface GenderApiComponent {

    @me.tatarka.inject.annotations.Provides
    fun provideGendersApi(retrofit: Retrofit): GendersApi {
        return retrofit.create(GendersApi::class.java)
    }
}
