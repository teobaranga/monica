package com.teobaranga.monica.contact.data

import com.teobaranga.monica.contact.data.remote.ContactApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@ContributesTo(AppScope::class)
interface MockContactApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun mockContactApi(): ContactApi = mockk<ContactApi>()
}
