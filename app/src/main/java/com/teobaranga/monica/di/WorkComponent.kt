package com.teobaranga.monica.di

import android.content.Context
import androidx.work.WorkManager
import com.teobaranga.monica.inject.runtime.ApplicationContext
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface WorkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
