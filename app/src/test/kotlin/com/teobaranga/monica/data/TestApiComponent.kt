package com.teobaranga.monica.data

import com.teobaranga.monica.contacts.data.ContactApi
import io.mockk.mockk
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(
    scope = AppScope::class,
    replaces = [ApiComponent::class],
)
interface TestApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideContactApi(): ContactApi = mockk<ContactApi>()
}
