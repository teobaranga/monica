package com.teobaranga.monica.contact

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.contact.detail.contactDetail
import com.teobaranga.monica.contact.edit.contactEdit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

/**
 * Contributes the contact destinations to the app's navigation entries.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object ContactEntryBuilders {

    @Provides
    @IntoSet
    fun provideContactEntries(): EntryProviderScope<NavKey>.() -> Unit = {
        contactDetail()
        contactEdit()
    }
}
