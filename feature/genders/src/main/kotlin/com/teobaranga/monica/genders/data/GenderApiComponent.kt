package com.teobaranga.monica.genders.data

import me.tatarka.inject.annotations.Provides
import retrofit2.Retrofit
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface GenderApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideGendersApi(retrofit: Retrofit): GendersApi {
        return retrofit.create(GendersApi::class.java)
    }
}
