package com.teobaranga.monica.journal

import androidx.lifecycle.ViewModel
import com.teobaranga.monica.journal.list.JournalEntryListViewModel
import com.teobaranga.monica.journal.view.JournalEntryViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass

/**
 * Exists to suppress "unused bindings" warning due to these [ViewModel] maps never being used in tests.
 */
@BindingContainer
@ContributesTo(
    scope = AppScope::class,
    replaces = [JournalEntryListViewModel::class, JournalEntryViewModel.Factory::class],
)
object FakeViewModelMap {

    @Provides
    fun emptyViewModelMap(): Map<KClass<out ViewModel>, ViewModel> = emptyMap()

    @Provides
    fun emptyAssistedViewModelMap(): Map<KClass<out ViewModel>, ViewModelAssistedFactory> = emptyMap()
}
