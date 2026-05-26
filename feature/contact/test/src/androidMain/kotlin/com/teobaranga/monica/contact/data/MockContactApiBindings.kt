package com.teobaranga.monica.contact.data

import com.teobaranga.monica.contact.data.remote.ContactApi
import com.teobaranga.monica.contact.data.remote.ContactApiImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.mockk.mockk

@BindingContainer
@ContributesTo(AppScope::class, replaces = [ContactApiImpl::class])
object MockContactApiBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun mockContactApi(): ContactApi = mockk<ContactApi>()
}
