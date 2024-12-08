package com.teobaranga.monica.di

import android.content.Context
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import com.r0adkll.kimchi.annotations.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface WorkComponent {

    @me.tatarka.inject.annotations.Provides
    @SingleIn(AppScope::class)
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
