package com.teobaranga.monica.contact.data

import com.teobaranga.monica.contact.data.remote.ContactApi
import io.mockk.mockk
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface MockContactApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun mockContactApi(): ContactApi = mockk<ContactApi>()
}
