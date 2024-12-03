package com.teobaranga.monica.log

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import timber.log.Timber

@ContributesTo(AppScope::class)
interface TimberTreeComponent {

    @Provides
    @IntoSet
    @SingleIn(AppScope::class)
    fun provideDebugTree(): Timber.Tree = Timber.DebugTree()
}
