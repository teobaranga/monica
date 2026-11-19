package com.teobaranga.monica.activity

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.activity.edit.ui.contactActivityEdit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

/**
 * Contributes the activity destinations to the app's navigation entries.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object ActivityEntryBuilders {

    @Provides
    @IntoSet
    fun provideActivityEntries(): EntryProviderScope<NavKey>.() -> Unit = {
        contactActivityEdit()
    }
}
